!> Programme unique de code_Carmel
!! @authors
!! - Yvonnick LE MENACH (L2EP),
!! - Georges MARQUES (L2EP),
!! - Emmanuel CAGNIOT (LIFL),
!! - Thomas HENNERON (L2EP),
!! - Loïc CHEVALLIER (L2EP),
!! - Olivier BOITEAU (EDF R&D),
!! - Julien KORECKI (L2EP).
!!
!!       +----------------------------------------------------------------------------+
!!       | FICHIER   : carmel.F90                                                     |
!!       | PROGRAMME : carmel.                                                        |
!!       | LANGAGE   : fortran 90/95.                                                 |
!!       | OBJET     : elements finis en 3D pour l'electromagnetisme.                 |
!!       | AUTEURS   : - Yvonnick LE MENACH (L2EP).                                   |
!!       |           : - Georges MARQUES (L2EP).                                      |
!!       |           : - Emmanuel CAGNIOT (LIFL).                                     |
!!       |           : - Loïc CHEVALLIER (L2EP).                                      |
!!       | CREATION  : xx.xx.xx.                                                      |
!!       | REVISION  : xx.xx.xx.                                                      |
!!       | REMARQUES : - importe "structureDonnees", "geometrie", "configuration",    |
!!       |           :   "formulationHsOmega" et "formulationA".                      |
!!       +----------------------------------------------------------------------------+
!

    PROGRAM carmel
     USE structureDonnees
     USE utilitaires
     USE geometrie
     USE io
     USE configuration_initiale
     !USE configuration
     USE formulationHsOmega
     USE formulationA
     USE EulerNewton
     USE CalculFlux
     USE periodiciteMouvement
     use posTraitementDonnees
     use circuit
     ! module Intel Fortran permettant certaines routines portables d'extension de Fortran 2003
     !use IFPORT

#if USE_MPI==1
     use mpi
#endif

     IMPLICIT NONE

#if USE_MPI==1
     !include 'mpif.h'
#endif


       !> Indique s'il faut continuer à proposer le menu du programme (.true.) ou quitter le programme (.false.)
       LOGICAL :: continuer
       !> Indice de boucle quelconque
       INTEGER :: i, j
       !> Choix de l'enchaînement :
       !!
       !! 0: sortir,
       !!
       !! 1: résolution+post-traitement en formulation A ou A-PHi,
       !!
       !! 2: résolution+post-traitement en formulation Omega ou T-Omega,
       !!
       !! 3: post-traitement en formulation A ou A-PHi,
       !!
       !! 4: post-traitement en formulation Omega ou T-Omega.
       !
       integer :: choix, choixFormulation
       !> Taille mémoire en octets (long)
       integer(kLong) :: taille
       !> Conversion d'un entier (long) en chaîne de caractère
       !! L'entier normal court avec signe, sur 4 octets, en machine 32 bits utilise 11 caractères
       !! L'entier long utilisera moins de 22 caractères.
       CHARACTER(len=22) :: entierTexte
       !> temps cpu initial et final
       real(kind(0d0)) :: tempsCPUinitial, tempsCPUfinal, dtCPU
       !> Nombre maximal d'éléments par noeud
       integer :: nombreMaximalDegreNoeudElement
       !> Tableau temporaire pour stocker les éléments d'un noeud donné
       !integer, dimension(:), allocatable :: listeIndicesElements
       !> Descripteur de fichier local, e.g., pour la sauvegarde/restauration du maillage
       integer :: descripteurSauvegarde
       !> Valeur d'Euler-Poincaré
       integer :: eulerPoincare
       !> Nombre d'arêtes réelles pour un élément donné
       integer :: nbAretes
       !> Nombre de facettes réelles pour un élément donné
       integer :: nbFacettes
       !> Indice de l'élément de référence pour un élément donné
       integer :: iref
       !> Nom de fichier de sauvegarde
       character(len=30) :: nom
       !> Test d'existence d'un fichier via la routine inquire
       logical :: existe
       !> conversion d'un entier en chaîne
       character(len=20) :: i2c
       !> Mode MPI+OpenMP hybride utilisé en définitive
       integer :: mpi_provided


        ! mode de déboguage ou pas
#if DEBUG==1
        debug = .true.
#else
        debug = .false.
#endif

        ! les message sont affichés à l'écran par défaut
        stdout = 6

        ! Nom de la machine sur laquelle le code est lancé
        ! en utilisant la fonction intrinsèque HOSTNM disponible pour GNU et Intel
        ! Le nom de la machine n'est pas connu a priori
        !hostname = ''
        !erreur = HOSTNM(hostname)
        !call testErreur(erreur,"Le nom de la machine ne peut pas etre connu",'carmel',arret=.false.)

        ! Identifiant Linux (pid)
        !pid = GETPID()

#if USE_MPI==1
         ! initialisation de MPI
         call MPI_INIT_THREAD(MPI_THREAD_FUNNELED,mpi_provided,erreur)
         ! On récupère le rang du processus courant, pour ne pas dupliquer certaines actions dans le code
         call MPI_COMM_RANK(MPI_COMM_WORLD, MPIrang, erreur)
         ! Test d'erreur sur le mode, qui arrête proprement le code en cas d'erreur
         !if (MPIrang == 0 .and. mpi_provided < MPI_THREAD_FUNNELED) then
         ! call stopErreur("Mode MPI_THREAD_FUNNELED non disponible",'carmel',arret=.false.)
         ! call MPI_ABORT(MPI_COMM_WORLD,1,erreur)
         !endif
         ! On définit la sortie standart dans un fichier fort.(rang+1000) si le rang est supérieur à 0, ou l'écran sinon
         ! Ce décalage de 1000 permet de passer les sorties standart, e.g., 6 pour écran sans perturber l'affichage,
         ! ainsi que d'éviter des conflits éventuels avec des sorties mal maîtrisées, e.g., write(113,*) ...,
         ! ou des fichiers créés correctement par le code avec des descripteurs entre ionum_min=10 et ionum_max=99
         ! Test de ionum_max < 1000 sinon erreur
         if (MPIrang == 0 .and. ionum_max > 1000) then
            write(message,*) "Le descripteur de fichier peut avoir un numero maximal ionum_max = ",ionum_max,&
             "qui depasse 1000, ce qui entrainera un conflit dans le contexte d'utilisation de MPI",&
             ", ou l'affichage de chaque processus MPI, a partir du rang 1, est dans le fichier fort.1000+rang",&
             ", i.e., fort.1001, fort.1002, etc."
            call stopErreur(message,'carmel',contexte=kErreurTaille)
         end if
         !
         if (MPIrang > 0) stdout = MPIrang+1000
         ! On récupère le nombre de processus MPI
         call MPI_COMM_SIZE(MPI_COMM_WORLD, MPIprocs, erreur)
         ! message
         write(message,*) "Parallelisme MPI en utilisant ",MPIprocs," processus."
         call writeMessage(message,'warning','carmel')
         ! On récupère le nom de la machine sur laquelle tourne le processus courant
         call MPI_GET_PROCESSOR_NAME(MPIhostname, MPIhostnamelength, erreur)
         ! Création d'un fichier indiquant le rang du processus MPI
         ! afin de permettre au script de lancement de connaître le dossier de rang 0 (master)
         ! pour récupérer les résultats de la simulation
         call creerDescripteur(descrip)
         write(i2c,*) MPIrang
         write(nom,*) 'mpi_rang_',trim(adjustl(i2c)),'.txt'
         open(descrip, file=trim(adjustl(nom)), form='formatted', status='unknown')
         write(descrip,*) 'Nombre processus MPI = ', MPIprocs
         write(descrip,*) 'Rang MPI de ce processus = ', MPIrang
         write(descrip,*) 'Nom de la machine hebergeant ce processus MPI = ', MPIhostname(1:MPIhostnamelength)
         close(descrip)
         call detruireDescripteur(descrip)
#endif

        ! lecture de la mémoire utilisée dans le code à ce moment,
        ! i.e., la mémoire statique car aucun tableau dynamique n'est créé encore
        memoireStatiqueLue = read_vmpeak() * 1024**2 ! conversion en octets du résultat en Mo.

       ! Presentation de l'application.
       write(stdout,*)  ""
       write(stdout,*)  "Identification de l'application:"
       write(stdout,*)  "--------------------------------"
       write(stdout,*)  trim(adjustl(kNomCode))
       write(stdout,*)  trim(adjustl(kAuteurs))
       write(stdout,*)  trim(adjustl(kMentionsLegales))
       write(stdout,*)  "Version ",trim(adjustl(kVersion)), " du ",trim(adjustl(kDate))
       write(stdout,*) ''

       ! info d'écriture des erreurs et avertissements dans le fichier kFichierLog
       write(message,*) "Les erreurs et avertissements du code sont disponibles dans le fichier : ", trim(adjustl(kFichierLog))
       call writeMessage(message, contexte='info', lieu='carmel')

       ! Ecriture de différentes informations dans le fichier kFichierLog
       ! Ecriture du nom de la machine s'il est connue
       !write(message,'(A,A)')  "Machine : ",trim(adjustl(hostname))
       !call writeMessage(message,'warning','carmel')
       ! Ecriture de l'identifiant Linux (pid) en mode debug
       !write(message,'(A,I10)')  "Identifiant Linux (pid) : ",pid
       !call writeMessage(message,'warning','carmel')


       ! Calcul du nombre de tâches OpenMP (1 si calcul séquentiel)
       nbthreads = 1
       !$OMP PARALLEL
       !$ nbthreads = OMP_GET_NUM_THREADS()
       !$OMP END PARALLEL
       if (nbthreads > 1) then
        write(message,'(A,I2,A)') "Execution // OpenMP avec ",nbthreads," threads."
       else
        write(message,'(A,I2,A)') "Execution sequentielle sur 1 processeur/coeur."
       endif
       call writeMessage(message,'warning','carmel')


        ! initialisation du code hors-configuration, e.g., indice de pas de temps non défini
        call initialiserCode

        ! initialisation de la configuration, à des valeurs par défaut
        call initialiserConfiguration

        ! lecture de la configuration Fortran ensuite
        ! configuration du maillage
        call definirMaillage
        ! vérification obligatoire car on y définit les paramètres formatMaillage et fichierPostPro nécessaires par la suite.
        call verifierMaillage

        ! Lecture des paramètres de précision et calcul des listes de pas de temps pour toute la simulation
        call definirParametrePrecision
        ! vérification obligatoire
        call verifierParametrePrecision

        ! message si en mode de déboguage
        if (debug) call writeMessage('Mode de deboguage (verbeux)...',contexte='warning',lieu='carmel')

       ! Lecture des noms de groupes du maillage, et sauvegarde dans un fichier texte si format MED.
       ! une fois seulement, avant tout pas de temps.
       if (formatMaillage == kMED) then
           if (trim(adjustl(sav%type)) == 'redemarrage' .and. sav%maillage) then
                ! message utilisateur
                write(message,*) 'Chargement des noms de groupes depuis le fichier : ',trim(adjustl(sav%nomMaillage)),'.'
                call writeMessage(message,'info','carmel')
                !
                ! vérification de l'existence du fichier contenant la sauvegarde
                existe = .false.
                inquire(FILE=trim(adjustl(sav%nomMaillage)),EXIST=existe)
                !
                if (existe) then
                    call lireMaillageSav(onlyGroupes=.true.)
                    ! debug : affichage des noms des groupes
                    if (debug) then
                        write(stdout,*) 'nombreGroupesNoeuds = ',nombreGroupesNoeuds
                        write(stdout,*) 'nombreGroupesElements = ',nombreGroupesElements
                        write(stdout,*) 'maillageMed%nbGroupes = ',maillageMed%nbGroupes
                        do i=1,maillageMed%nbGroupes
                            write(stdout,*) i,trim(adjustl(maillageMed%listeNomGroupe(i)))
                        end do
                    end if
                else ! le fichier de sauvegarde n'existe pas
                   ! message utilisateur d'erreur non critique
                   call stopErreur(sav%nomMaillage,'carmel',contexte=kErreurFichierAbsent,arret=.false.)
                   write(message,*) "La lecture des noms de groupes va etre realisee a partir du fichier de maillage original"
                   call writeMessage(message,"info","carmel")
                endif
           endif
           !
           if (.not.(trim(adjustl(sav%type)) == 'redemarrage' .and. sav%maillage .and. groupesMaillageLus)) then
              write(stdout,*)  ""
              ! avertissement de test maillage
              call writeMessage("Maillage MED : Vérification de la conformité et lecture des noms des groupes",&
                                contexte='info', lieu='carmel')
              write(stdout,*)  ""
              CALL lireNomsGroupesMaillageMed( fichierMaillage )
              ! tout s'est bien passé (couleur verte)
              call writeMessage("Lecture des noms des groupes", contexte='ok', lieu='carmel')
              ! Arrêt du programme en cas d'erreur
              call testErreur(erreur,"Maillage non conforme ou probleme lecture des groupes",'carmel',arret=.true.)
              ! Il est important de sauvegarder cette information de groupes lus
              groupesMaillageLus = .true.
          endif
       endif

        ! calcul et sauvegarde de tous les temps possible.
        ! Obtention du nombre de pas de temps est connu
        ! Cette étape doit être réalisée avant l'appel à configurer,
        ! car on "appelle" une partie de configurer pour tous les pas de temps
        ! ce qui peut modifier la configuration initiale au pas de temps 0 (cf. plus bas)
        ! création des tableaux
        allocate(listeDt(NBoucleTemps),stat=erreur)
        call testCreation(erreur,'listeDt','carmel',global=.true.,taille=NBoucleTemps*kReal,arret=.true.)
        ! initialisation à 0
        listeDt(:) = 0.d0
        !
        allocate(listeTemps(NBoucleTemps),stat=erreur)
        call testCreation(erreur,'listeTemps','carmel',global=.true.,taille=NBoucleTemps*kReal,arret=.true.)
        ! initialisation à 0
        listeTemps(:) = 0.d0
        ! calcul des temps
        do indBoucleTemps=1,NBoucleTemps
            ! rappel de la valeur lue dans le fichier XML, éventuellement
            dt = listeDt(indBoucleTemps)
            ! appel aux routines de configuration définissant le temps
            ! Lecture des paramètres de précision et calcul des listes de pas de temps pour toute la simulation
            call definirParametrePrecision
            ! vérification optionnelle
            if (trim(adjustl(checkConfiguration)) == 'complete') call verifierParametrePrecision
            ! la variation du temps peut aussi avoir lieu dans la routine definirSource
            call definirSource
            !
            listeDt(indBoucleTemps) = dt
            if (indBoucleTemps > 1) then
                listeTemps(indBoucleTemps) = dt + listeTemps(indBoucleTemps-1)
            else
                listeTemps(indBoucleTemps) = dt
            endif
        enddo

        ! retour à l'indice original et configuration hors-temps
        indBoucleTemps = 0
        call configurer

       ! Configuration de l'application.
       write(stdout,*)  ""
       write(stdout,*)  "Configuration de l'application:"
       write(stdout,*)  "-------------------------------"

       ! initialisation de l'indice de pas de temps à 0
       indBoucleTemps = 0
       dt = 0.d0

       ! Choix des interpolations.
       CALL pointsGaussTriangleRef6o4
       write(stdout,*)  " - Interpolation sur le triangle : ok"
       CALL pointsGaussRectangleRef7o5
       write(stdout,*)  " - Interpolation sur le rectangle: ok"
       CALL pointsGaussTetraedreRef4o2
       ! CALL pointsGaussTetraedreRef15o5
       write(stdout,*)  " - Interpolation sur le tetraedre: ok"
       CALL pointsGaussPrismeRefxox
       write(stdout,*)  " - Interpolation sur le prisme   : ok"
       CALL pointsGaussHexaedreRef3o6
       write(stdout,*)  " - Interpolation sur l'hexaedre  : ok"
       !
       CALL definirTypeProbleme

       ! Le format EDF (med2edf.f90) commence par n_nombreLexicographique_nom, e.g. n_01_air_face1, n_02_plaque_face2..., n_10_toto.
       ! Cette identification est fixée dans le code.
       ! identification automatique (lexicographique), i.e., les premiers groupes MED de noeuds sont dans l'ordre. Rien à faire.
       ! Appariemment automatique entre nom du groupe et indice du milieu
       if (modeNomGroupeMed == kNomGroupeMed2EDF) then
            write(message,*) "Appariemment automatique entre indices des conditions limites des champs",&
             " et noms de groupes du maillage MED selon la convention de nommage EDF (n_)."
             call writeMessage(message, contexte='warning', lieu='carmel')
       end if

       ! Transcription des conditions limites sur les potentiels.
       CALL definirConditionsLimitesPot
       !
       call writeMessage("Configuration de la geometrie : ok",contexte='ok',lieu='carmel')
       ! test des erreurs éventuelles
       CALL testErreur(erreur,'erreur non detaillee','carmel',arret=.true.)


        ! Ce problème est-il non-linéaire ?
        ! Il est linéaire par défaut
        problemeNonLineaire = .false.
        ! boucle sur tous les milieux pour déterminer si le problème est linéaire ou non
        do i=1, nombreMilieux
            if (listeMilieux(i)%coefficientsMarrocco(1) /= 0.0d0) then
                ! on a trouvé un milieu non-linéaire : le problème est non-linéaire
                problemeNonLineaire = .true.
                ! sortie de boucle, on sait que le problème est non-linéaire
                exit
            endif
        enddo
        ! message
        if (problemeNonLineaire) then
            write(stdout,*) ' - Probleme non-lineaire.'
        else
            write(stdout,*) ' - Probleme lineaire.'
            ! On force le choix Raznewton = .true. par défaut pour un problème non-linéaire
            ! Avertissement utilisateur en tous les cas
            if (.not.Raznewton) then
                write(message,*) "On impose par defaut Raznewton = .true. pour un probleme lineaire,",&
                 " si l'utilisateur n'a fait aucun choix particulier."
                call writeMessage(message,'warning','carmel')
            end if
            Raznewton = .true.
        endif

        ! configuration complète du code avec couplage-circuit, si pris en compte.
        if (lectureQucs) then
            call lectureNetlistQucs
            call correspondancequcsinducteurs
        end if

       ! Lecture du maillage à partir du fichier (formats UNV ou MED).
       write(stdout,*)  ""
       write(stdout,*)  "Caracteristiques du maillage:"
       write(stdout,*)  "-----------------------------"
       ! calcul du temps de lecture du maillage. Temps initial
       call cpu_time(tempsCPUinitial)

       ! Chargement du maillage sauvegardé au préalable ou lecture du maillage et sauvegarde éventuelle
       if (trim(adjustl(sav%type)) == 'redemarrage' .and. sav%maillage) then
            ! message utilisateur
            write(message,*) 'Chargement du maillage depuis le fichier : ',trim(adjustl(sav%nomMaillage)),'.'
            call writeMessage(message,'info','carmel')
            !
            ! vérification de l'existence du fichier contenant la sauvegarde
            existe = .false.
            inquire(FILE=trim(adjustl(sav%nomMaillage)),EXIST=existe)
            !
            if (existe) then
                call lireMaillageSav
            else ! le fichier de sauvegarde n'existe pas
               ! message utilisateur d'erreur non critique
               call stopErreur(sav%nomMaillage,'carmel',contexte=kErreurFichierAbsent,arret=.false.)
               call writeMessage("La lecture du maillage va etre realise","info","carmel")
            endif
       endif

      ! Lecture du maillage, si non déjà chargé
      if (.not.(trim(adjustl(sav%type)) == 'redemarrage' .and. sav%maillage .and. sav%maillageCharge)) then
           ! message
           write(stdout,*) 'Lecture du maillage'
           ! lecture du maillage
           call lireMaillage(fichierMaillage)
           !
      endif

      ! Sauvegarde du maillage lors du mode (type) de sauvegarde ou de redémarrage
      if ((trim(adjustl(sav%type))=='sauvegarde'.or.trim(adjustl(sav%type))=='redemarrage')&
           .and.sav%maillage .and. (.not. sav%maillageCharge)) then
           !
           call sauveMaillage
           !
       end if ! sauvegarde du maillage soumise à condition

       ! Il est important de sauvegarder cette information de groupes lus
       groupesMaillageLus = .true.
       ! contrôle de cohérence sur le nombre de milieux
       if (nombreMilieux > nombreGroupesElements) then
            write(message,*) 'Le nombre de milieux : ',nombreMilieux,&
                " est plus grand que le nombre de groupes d'elements : ",nombreGroupesElements
            call stopErreur(message,'carmel',arret=.true.)
       endif
       ! Optimisation de la mémoire pour les éléments connectés aux noeuds
       ! lecture du nombre maximal d'éléments par noeuds
       nombreMaximalDegreNoeudElement = maxval(listeNoeuds(:)%degreNoeudElements)
       if (debug) write(stdout,*) "DEBUG: nombre maximal d'elements par noeud = ",nombreMaximalDegreNoeudElement

       ! calcul du temps de lecture du maillage. Temps final
       call cpu_time(tempsCPUfinal)
       !
       if (debug) write(stdout,*) 'DEBUG: temps de lecture du maillage (s) = ',tempsCPUfinal-tempsCPUinitial
       !
       ! Affichage du nombre de noeuds et d'éléments
       write(stdout,*)  " - Lecture           : ok"
       write(stdout,*)  " - Dispositif        : ", trim(adjustl(identification))
       write(stdout,*)  " - Maillage          : ", trim(adjustl(fichierMaillage))
       write(stdout,*)  " Statistiques lues dans le maillage (a completer plus loin) :"
       write(stdout,*)  " - Nombre de noeuds  : ", nombreNoeuds
       write(stdout,*)  " - Nombre d'elements : ", nombreElements
       ! affichage du nombre d'éléments par type de référence
       do i=1,kNombreElementsReference
          write(stdout,*)  " -- Nombre d'elements de type ",kNomTypeElement(i)," : ", nombreElementsParType(i)
       enddo

        ! Erreur lorsque pyramides dans le maillage, car elles ne sont pas prises en compte
        if (nombreElementsParType(kIndicePyramide) > 0) then
            call stopErreur("Ce maillage contient des pyramides, dont le code ne tient pas compte",'carmel',arret=.true.)
        endif
        ! arrêt si le nombre de noeuds ou d'éléments dépasse le plus grand entier admissible en 32bits,
        ! i.e., 2^31 = 2147483647 (2 milliards), pour la constante kCdl liée au conditionnement des inconnues.
        if (nombreNoeuds >= kCdl .or. nombreElements >= kCdl) then
            call stopErreur("Ce maillage contient trop de noeuds ou d'elements (> a peu pres 2 milliards)",&
                            'carmel',arret=.true.)
        endif
        ! arret en cas d'erreur
        call testErreur(erreur,'test au hasard','carmel',arret=.true.)

        ! création des tableaux dépendant du maillage
        allocate(postraitementdonne%pertes%loci_elements(nombreElements),stat=erreur)
        call testCreation(erreur,'postraitementdonne%pertes%loci_elements','carmel',global=.true.,&
                            taille=nombreElements*kInteger,arret=.true.)
        postraitementdonne%pertes%loci_elements(:) = kNil
        !
        allocate(postraitementdonne%pertes%fft_elements(nombreElements),stat=erreur)
        call testCreation(erreur,'postraitementdonne%pertes%fft_elements','carmel',global=.true.,&
                            taille=nombreElements*kInteger,arret=.true.)
        postraitementdonne%pertes%fft_elements(:) = kNil
        !
        allocate(postraitementdonne%carteChampsElements(kNombreMaximumChamps,nombreElements+nombreNoeuds),stat=erreur)
        call testCreation(erreur,'postraitementdonne%carteChampsElements','carmel',global=.true.,&
                            taille=kNombreMaximumChamps*(nombreElements+nombreNoeuds)*kLogical,arret=.true.)
        postraitementdonne%carteChampsElements(:,:) = 0

       ! configuration du pré- et post-traitement, hors configurer
       call definirPretraitement
       call definirPostraitement
       if (trim(adjustl(checkConfiguration)) /= 'non') call verifierPostraitement

       ! vérification de la configuration pour tous les pas de temps, optionnelle
       if (trim(adjustl(checkConfiguration)) == 'complete') then
           call writeMessage("Verification de la configuration pour toute la simulation...",contexte='info',lieu='carmel')
           do i = 1, NBoucleTemps
              indBoucleTemps = i
              call configurer
           end do
           call writeMessage("... configuration verifiee (erreurs eventuelles non critiques).",contexte='ok',lieu='carmel')
           ! retour à l'initialisation sans pas de temps
           indBoucleTemps = 0
           call configurer
       endif

        ! En cas de problème non-linéaire, séparation de l'assemblage linéaire et non-linéaire
        ! si demandé par l'utilisateur
        if (problemeNonLineaire .and. separerAssemblageLNL) then
            ! message utilisateur
            call writeMessage("Separation de l'assemblage lineaire / non-lineaire","warning",'carmel')
            ! Création de la liste des éléments non-linéaires
            ! Cela peut prendre du temps, alors on avertit l'utilisateur
            call writeMessage("Creation de la liste des elements lineaire et non-lineaires en cours...","info",'carmel')
            ! On compte le nombre d'éléments non-linéaires, pour en créer la liste à la bonne taille
            nombreElementsNL = 0
            do i=1, nombreElements
                if (listeMilieux(listeElements(i)%indiceMilieu)%coefficientsMarrocco(1) /= 0.d0) &
                    nombreElementsNL = nombreElementsNL + 1
            end do
            if (nombreElementsNL > 0) then
                allocate(listeElementsNL(nombreElementsNL),stat=erreur)
                call testCreation(erreur,'listeElementsNL','carmel',global=.true.,taille=nombreElementsNL*kInteger,arret=.false.)
                if (erreur /= kAucuneErreur) then
                    call writeMessage("Annulation de la separation de l'assemblage lineaire / non-lineaire","warning",'carmel')
                    separerAssemblageLNL = .false.
                else
                    ! création de la liste des éléments linéaires
                    nombreElementsL = nombreElements - nombreElementsNL
                    allocate(listeElementsL(nombreElementsL),stat=erreur)
                    call testCreation(erreur,'listeElementsL','carmel',global=.true.,taille=nombreElementsL*kInteger,arret=.false.)
                    if (erreur /= kAucuneErreur) then
                        call writeMessage("Annulation de la separation de l'assemblage lineaire / non-lineaire","warning",'carmel')
                        separerAssemblageLNL = .false.
                    else
                        ! remplissage des 2 listes
                        nombreElementsNL = 0
                        nombreElementsL = 0
                        do i=1, nombreElements
                            if (listeMilieux(listeElements(i)%indiceMilieu)%coefficientsMarrocco(1) /= 0.d0) then
                                nombreElementsNL = nombreElementsNL + 1
                                listeElementsNL(nombreElementsNL) = i
                            else
                                nombreElementsL = nombreElementsL + 1
                                listeElementsL(nombreElementsL) = i
                            endif
                        end do
                        call writeMessage("Creation de la liste des elements non-lineaires","ok",'carmel')
                        if (debug) then
                            write(stdout,*) "Nombre d'elements non-lineaires : ",nombreElementsNL
                            write(stdout,*) "Nombre d'elements lineaires : ",nombreElementsL
                        end if
                    endif
                end if
            else
                call stopErreur("Aucun element non-lineaire",'carmel',contexte=kErreurConfiguration,arret=.false.)
                separerAssemblageLNL = .false.
            end if
        end if ! separerProblemeLNL

      ! chargement des points explorateurs du champ sauvegardés au préalable et définis dans la configuration
      if (trim(adjustl(sav%type))=='redemarrage' &
           .and. sav%pointsExplorateurs .and. (.not. sav%pointsExplorateursCharge)&
           .and. sum(pointsExplorateursChamps(:)%nb) > 0) then
            ! on stocke la solution pour chaque iteration
            write(message,*) 'Chargement des points explorateurs du champ a partir du fichier : ',&
                                trim(adjustl(sav%nomPointsExplorateurs)),'.'
            call writeMessage(message,'info','carmel')
            ! vérification de l'existence du fichier contenant la sauvegarde
            existe = .false.
            inquire(FILE=trim(adjustl(sav%nomPointsExplorateurs)),EXIST=existe)
            if (existe) then
                !
                ! Ouverture du fichier en ecriture (descripteur global)
                call creerDescripteur(descripteurSauvegarde)
                open(unit=descripteurSauvegarde, file=sav%nomPointsExplorateurs,form='unformatted',STATUS='unknown')
                ! boucle sur tous les points explorateurs possibles et sauvegarde de ceux définis seulement
                do i = 1, kNombreMaximumGroupesPointsExplorateursChamp
                    if (pointsExplorateursChamps(i)%nb > 0) then
                        ! sauvegarde en tout premier lieu du nombre de points explorateurs pour ce type
                        read(descripteurSauvegarde) pointsExplorateursChamps(i)%nb
                        ! debug : affichage du nombre de points explorateurs définis
                        if (debug) then
                            write(message,*) "Nombre de points explorateurs definis dans le fichier ",&
                                                trim(adjustl(sav%nomPointsExplorateurs))," : ", pointsExplorateursChamps(i)%nb
                            call writeMessage(message,'debug','carmel')
                        endif
                        ! sauvegarde ensuite des tableaux dynamiques
                        do j = 1, pointsExplorateursChamps(i)%nb
                            read(descripteurSauvegarde) pointsExplorateursChamps(i)%coordRef(j,1:3)
                            read(descripteurSauvegarde) pointsExplorateursChamps(i)%elements(j)
                            read(descripteurSauvegarde) pointsExplorateursChamps(i)%ielt(j)
                            ! Affichage des valeurs lues en mode de déboguage
                            if (debug) &
                            write(stdout,'(1P,3(1X,E22.15),2(1X,I8),3(1X,E22.15),1X,I4,1X,E8.1)') &
                            pointsExplorateursChamps(i)%coord(j,1:3), pointsExplorateursChamps(i)%elements(j),&
                            pointsExplorateursChamps(i)%ielt(j), pointsExplorateursChamps(i)%coordRef(j,1:3), 0, 0.d0
                        end do
                    endif ! ce point explorateur est défini
                enddo ! boucle sur tous les points explorateurs possibles
                ! fermeture du fichier
                close(descripteurSauvegarde)
                call detruireDescripteur(descripteurSauvegarde)
                ! Les points explorateurs ont été chargés, pas la peine de les calculer ensuite
                sav%pointsExplorateursCharge = .true.
            else ! le fichier de sauvegarde n'existe pas
               ! message utilisateur d'erreur non critique
               call stopErreur(sav%nomPointsExplorateurs,'carmel',contexte=kErreurFichierAbsent,arret=.false.)
               call writeMessage("Le calcul des points explorateurs du champ va etre realise","info","carmel")
           endif ! test d'existence du fichier de sauvegarde
      end if ! sauvegarde soumise à condition

      ! Calcul des points explorateurs du champ, si non déjà chargé
      if (.not.(trim(adjustl(sav%type)) == 'redemarrage' .and. sav%pointsExplorateurs .and. sav%pointsExplorateursCharge)) then
          ! recherche (calcul) des points explorateurs avant orientation des éléments,
          ! i.e., recherche des éléments contenant les points seulement
          call rechercherPointsExplorateurs(localisation=.false.)
      endif

      ! Orientation de tous les elements.
      write(stdout,*) "Orientation des elements..."
      CALL orienterElements

      ! Calcul des points explorateurs du champ, si non déjà chargé
      if (.not.(trim(adjustl(sav%type)) == 'redemarrage' .and. sav%pointsExplorateurs .and. sav%pointsExplorateursCharge)) then
          ! recherche (calcul) des points explorateurs après orientation des éléments,
          ! i.e., recherche des coordonnées des points dans les éléments de référence
          call rechercherPointsExplorateurs(localisation=.true.)
      endif

      ! sauvegarde des points explorateurs du champ lors du mode (type) de sauvegarde ou de redémarrage
      ! si au moins l'un d'entre eux est défini
      if ((trim(adjustl(sav%type))=='sauvegarde'.or.trim(adjustl(sav%type))=='redemarrage')&
           .and.sav%pointsExplorateurs .and. (.not. sav%pointsExplorateursCharge)&
           .and. sum(pointsExplorateursChamps(:)%nb) > 0) then
            ! on stocke la solution pour chaque iteration
            write(message,*) 'Sauvegarde des points explorateurs du champ dans le fichier : ',&
                                trim(adjustl(sav%nomPointsExplorateurs)),'.'
            call writeMessage(message,'info','carmel')
            !
            ! Ouverture du fichier en ecriture (descripteur global)
            call creerDescripteur(descripteurSauvegarde)
            open(unit=descripteurSauvegarde, file=sav%nomPointsExplorateurs,form='unformatted',STATUS='unknown')
            ! boucle sur tous les points explorateurs possibles et sauvegarde de ceux définis seulement
            do i = 1, kNombreMaximumGroupesPointsExplorateursChamp
                if (pointsExplorateursChamps(i)%nb > 0) then
                    ! debug : affichage du nombre de points explorateurs définis
                    if (debug) then
                        write(message,*) "Nombre de points explorateurs definis dans le fichier ",&
                                            trim(adjustl(sav%nomPointsExplorateurs))," : ", pointsExplorateursChamps(i)%nb
                        call writeMessage(message,'debug','carmel')
                    endif
                    ! chargement en tout premier lieu du nombre de points explorateurs pour ce type
                    write(descripteurSauvegarde) pointsExplorateursChamps(i)%nb
                    ! chargement ensuite des tableaux dynamiques, qui ont déjà été créés lors de la vérification de la configuration
                    do j = 1, pointsExplorateursChamps(i)%nb
                        write(descripteurSauvegarde) pointsExplorateursChamps(i)%coordRef(j,1:3)
                        write(descripteurSauvegarde) pointsExplorateursChamps(i)%elements(j)
                        write(descripteurSauvegarde) pointsExplorateursChamps(i)%ielt(j)
                    end do
                endif ! ce point explorateur est défini
            enddo ! boucle sur tous les points explorateurs possibles
            ! fermeture du fichier
            close(descripteurSauvegarde)
            call detruireDescripteur(descripteurSauvegarde)
      end if ! sauvegarde soumise à condition

      ! Chargement des arêtes sauvegardées au préalable ou calcul et sauvegarde éventuelle
      if (trim(adjustl(sav%type)) == 'redemarrage' .and. sav%aretes) then
            ! message utilisateur
            write(message,*) 'Chargement des aretes depuis le fichier : ',&
                                trim(adjustl(sav%nomAretes)),'.'
            call writeMessage(message,'info','carmel')
            ! vérification de l'existence du fichier contenant la sauvegarde
            existe = .false.
            inquire(FILE=trim(adjustl(sav%nomAretes)),EXIST=existe)
            if (existe) then
                ! Ouverture du fichier en lecture (descripteur global)
                call creerDescripteur(descripteurSauvegarde)
                open(unit=descripteurSauvegarde, file=sav%nomAretes,form='unformatted',STATUS='unknown')
                ! lecture en tout premier lieu du nombre d'arêtes,
                ! dans une variable locale car ce nombre est déjà connu par le système
                read(descripteurSauvegarde) nombreAretes
                ! debug : affichage du nombre de noeuds lues dans le fichier
                if (debug) then
                    write(message,*) "Nombre d'aretes definies dans le fichier ",&
                                        trim(adjustl(sav%nomAretes))," : ", nombreAretes
                    call writeMessage(message,'debug','carmel')
                endif
                ! création du tableau contenant la liste des arêtes
                allocate(listeAretes(nombreAretes),stat=erreur)
                call testCreation(erreur,'listeAretes','carmel',global=.true.,taille=nombreAretes*kTailleARETE,arret=.true.)
                ! lecture de la suite du fichier
                ! lecture de l'association arêtes / noeuds
                do i = 1, nombreAretes
                    read(descripteurSauvegarde) listeAretes(i)%indiceNoeudDepart
                    read(descripteurSauvegarde) listeAretes(i)%indiceNoeudArrivee
                    ! initialisation de la circulation de H sur les arêtes
                    listeAretes(i)%circulationH = 0.d0
                end do
                ! lecture de l'association éléments / arêtes
                do i = 1, nombreElements
                    iref = listeElements(i)%indiceElementReference
                    nbAretes = listeElementsReference(iref)%nombreAretes
                    do j = 1, nbAretes
                        read(descripteurSauvegarde) listeElements(i)%listeIndicesAretes(j)
                    end do
                end do
                ! fermeture du fichier
                close(descripteurSauvegarde)
                call detruireDescripteur(descripteurSauvegarde)
                ! Indication des arêtes chargées
                sav%aretesChargees = .true.
                ! initialisation d'autres valeurs non sauvegardées
                ! pas de distinction a priori entre arête interne au milieu et arête de surface
                ! cette valeur est importante pour le test par défaut (clJindn0Tmonogroupe = .false.)
                ! des arêtes conditionnées (routine areteConditionnee, module utilitaires.F90)
                listeAretes(:)%areteinternemilieu = .false.
            else
               ! message utilisateur d'erreur non critique
               call stopErreur(sav%nomAretes,'carmel',contexte=kErreurFichierAbsent,arret=.false.)
               call writeMessage("Le calcul des aretes va etre realise","info","carmel")
            endif
            !
      endif

      ! Calcul des aretes du maillage, si non déjà chargé
      if (.not.(trim(adjustl(sav%type)) == 'redemarrage' .and. sav%aretes .and. sav%aretesChargees)) then
           !
           write(stdout,*) "calcul des aretes..."
           !
           CALL calculerAretes(erreur)
           !
           call testErreur(erreur,"test d'erreur apres appel a la routine calculerAretes",'carmel',contexte=erreur,arret=.true.)
           !
      endif

      ! Sauvegarde du maillage lors du mode (type) de sauvegarde ou de redémarrage
      if ((trim(adjustl(sav%type))=='sauvegarde'.or.trim(adjustl(sav%type))=='redemarrage')&
           .and.sav%aretes .and. (.not. sav%aretesChargees)) then
            ! on stocke la solution pour chaque iteration
            write(message,*) 'Sauvegarde des aretes dans le fichier : ',&
                                trim(adjustl(sav%nomAretes)),'.'
            call writeMessage(message,'info','carmel')
            !
            ! Ouverture du fichier en ecriture (descripteur global)
            call creerDescripteur(descripteurSauvegarde)
            open(unit=descripteurSauvegarde, file=sav%nomAretes,form='unformatted',STATUS='unknown')
            ! debug : affichage du nombre de noeuds lues dans le fichier
            if (debug) then
                write(message,*) "Nombre d'aretes definies dans le fichier ",&
                                    trim(adjustl(sav%nomAretes))," : ", nombreAretes
                call writeMessage(message,'debug','carmel')
            endif
            ! sauvegarde en tout premier lieu du nombre d'arêtes, i.e., la taille de listeAretes
            write(descripteurSauvegarde) nombreAretes
            ! sauvegarde ensuite de l'association arêtes / noeuds
            do i = 1, nombreAretes
                write(descripteurSauvegarde) listeAretes(i)%indiceNoeudDepart
                write(descripteurSauvegarde) listeAretes(i)%indiceNoeudArrivee
            end do
            ! sauvegarde ensuite de l'association éléments / arêtes
            do i = 1, nombreElements
                iref = listeElements(i)%indiceElementReference
                nbAretes = listeElementsReference(iref)%nombreAretes
                do j = 1, nbAretes
                    write(descripteurSauvegarde) listeElements(i)%listeIndicesAretes(j)
                end do
            end do
            ! fermeture du fichier
            close(descripteurSauvegarde)
            call detruireDescripteur(descripteurSauvegarde)
      end if ! sauvegarde soumise à condition


      ! Chargement des facettes sauvegardées au préalable ou calcul et sauvegarde éventuelle
      if (trim(adjustl(sav%type)) == 'redemarrage' .and. sav%facettes) then
            ! message utilisateur
            write(message,*) 'Chargement des facettes depuis le fichier : ',&
                                trim(adjustl(sav%nomFacettes)),'.'
            call writeMessage(message,'info','carmel')
            ! vérification de l'existence du fichier contenant la sauvegarde
            existe = .false.
            inquire(FILE=trim(adjustl(sav%nomFacettes)),EXIST=existe)
            if (existe) then
                ! Ouverture du fichier en lecture (descripteur global)
                call creerDescripteur(descripteurSauvegarde)
                open(unit=descripteurSauvegarde, file=sav%nomFacettes,form='unformatted',STATUS='unknown')
                ! lecture en tout premier lieu du nombre de facettes,
                ! dans une variable locale car ce nombre est déjà connu par le système
                read(descripteurSauvegarde) nombreFacettes
                ! debug : affichage du nombre de noeuds lues dans le fichier
                if (debug) then
                    write(message,*) "Nombre de facettes definies dans le fichier ",&
                                        trim(adjustl(sav%nomFacettes))," : ", nombreFacettes
                    call writeMessage(message,'debug','carmel')
                endif
                ! création du tableau contenant la liste des arêtes
                allocate(listeFacettes(nombreFacettes),stat=erreur)
                call testCreation(erreur,'listeFacettes','carmel',global=.true.,taille=nombreFacettes*kTailleFACETTE,arret=.true.)
                ! lecture de la suite du fichier
                ! lecture de l'association facettes / noeuds + arêtes + élements
                do i = 1, nombreFacettes
                    read(descripteurSauvegarde) listeFacettes(i)%listeIndicesNoeuds
                    read(descripteurSauvegarde) listeFacettes(i)%listeIndicesAretes
                    read(descripteurSauvegarde) listeFacettes(i)%listeIndicesElements
                    ! initialisation du flux de J normalisé sur les facettes
                    listeFacettes(i)%fluxJNormalise = 0.d0
                end do
                ! lecture de l'association éléments / facettes
                do i = 1, nombreElements
                    iref = listeElements(i)%indiceElementReference
                    nbFacettes = listeElementsReference(iref)%nombreFacettes
                    do j = 1, nbFacettes
                        read(descripteurSauvegarde) listeElements(i)%listeIndicesFacettes(j)
                    end do
                end do
                ! fermeture du fichier
                close(descripteurSauvegarde)
                call detruireDescripteur(descripteurSauvegarde)
                ! Indication des arêtes chargées
                sav%facettesChargees = .true.
            else
               ! message utilisateur d'erreur non critique
               call stopErreur(sav%nomFacettes,'carmel',contexte=kErreurFichierAbsent,arret=.false.)
               call writeMessage("Le calcul des facettes va etre realise","info","carmel")
            endif
            !
      endif

      ! Calcul des facettes du maillage, si non déjà chargé
      if (.not.(trim(adjustl(sav%type)) == 'redemarrage' .and. sav%facettes .and. sav%facettesChargees)) then
          ! Calcul des facettes du maillage.
          write(stdout,*) "calcul des facettes..."
          !
          CALL calculerFacettes(erreur)
          !
          call testErreur(erreur,"test d'erreur apres appel a la routine calculerFacettes",'carmel',contexte=erreur,arret=.true.)
          !
      endif

      ! Sauvegarde du maillage lors du mode (type) de sauvegarde ou de redémarrage
      if ((trim(adjustl(sav%type))=='sauvegarde'.or.trim(adjustl(sav%type))=='redemarrage')&
           .and.sav%facettes .and. (.not. sav%facettesChargees)) then
            ! on stocke la solution pour chaque iteration
            write(message,*) 'Sauvegarde des facettes dans le fichier : ',&
                                trim(adjustl(sav%nomFacettes)),'.'
            call writeMessage(message,'info','carmel')
            !
            ! Ouverture du fichier en ecriture (descripteur global)
            call creerDescripteur(descripteurSauvegarde)
            open(unit=descripteurSauvegarde, file=sav%nomFacettes,form='unformatted',STATUS='unknown')
            ! debug : affichage du nombre de noeuds lues dans le fichier
            if (debug) then
                write(message,*) "Nombre de facettes definies dans le fichier ",&
                                    trim(adjustl(sav%nomFacettes))," : ", nombreFacettes
                call writeMessage(message,'debug','carmel')
            endif
            ! sauvegarde en tout premier lieu du nombre de facettes, i.e., la taille de listeFacettes
            write(descripteurSauvegarde) nombreFacettes
            ! sauvegarde ensuite de l'association facettes / noeuds + arêtes + élements
            do i = 1, nombreFacettes
                write(descripteurSauvegarde) listeFacettes(i)%listeIndicesNoeuds
                write(descripteurSauvegarde) listeFacettes(i)%listeIndicesAretes
                write(descripteurSauvegarde) listeFacettes(i)%listeIndicesElements
            end do
            ! sauvegarde enfin de l'association éléments / facettes
            do i = 1, nombreElements
                iref = listeElements(i)%indiceElementReference
                nbFacettes = listeElementsReference(iref)%nombreFacettes
                do j = 1, nbFacettes
                    write(descripteurSauvegarde) listeElements(i)%listeIndicesFacettes(j)
                end do
            end do
            ! fermeture du fichier
            close(descripteurSauvegarde)
            call detruireDescripteur(descripteurSauvegarde)
      end if ! sauvegarde soumise à condition

       !
       write(stdout,*)  " Statistiques completes du maillage :"
       write(stdout,*)  " - Nombre de noeuds  : ", nombreNoeuds
       write(stdout,*)  " - Nombre d'aretes   : ", nombreAretes
       write(stdout,*)  " - Nombre de facettes: ", nombreFacettes
       write(stdout,*)  " - Nombre d'elements : ", nombreElements
       ! affichage du nombre d'éléments par type de référence
       do i=1,kNombreElementsReference
          write(stdout,*)  " -- Nombre d'elements de type ",kNomTypeElement(i)," : ", nombreElementsParType(i)
       enddo
       ! calcul d'Euler-Poincaré et message approprié
       eulerPoincare = nombreNoeuds - nombreAretes + nombreFacettes - nombreElements
       if (eulerPoincare == 1) then
        call writeMessage('Euler Poincare = 1', contexte='ok', lieu='carmel')
       else
        write(message,*) 'Euler Poincare = ',eulerPoincare,' (different de 1)'
        call writeMessage(message, contexte='warning', lieu='carmel')
       endif

       ! Acquisition des differents elements des methodes de calculs de flux.
       IF (nombreSpireExploratrice /= 0) THEN
        CALL stockageElementGeometrique
        write(stdout,*)  " "
        call writeMessage(" - Traitement des aretes des spires exploratrices", contexte='ok', lieu='carmel')
        write(stdout,*)  " "
       END IF

       ! Type de probleme.
       write(stdout,*) ' '
       write(stdout,*) ' '
       write(stdout,*)  "    -------------------------------------------"
       SELECT CASE(typeProbleme)
       CASE(kMagnetostatique)
        write(stdout,*)  "    |         PROBLEME MAGNETOSTATIQUE        |"
       CASE(kElectrocinetique)
        write(stdout,*)  "    |        PROBLEME ELECTROCINETIQUE        |"
       CASE(kMagnetodynamique)
        write(stdout,*)  "    |        PROBLEME MAGNETODYNAMIQUE        |"
       END SELECT
       write(stdout,*)  "    -------------------------------------------"

       ! info d'écriture des erreurs et avertissements dans le fichier kfichierLog
       ! Répétition pour que l'utilisateur le voit bien
       write(message,*) "Les erreurs et avertissements du code sont disponibles dans le fichier : ", trim(adjustl(kFichierLog))
       call writeMessage(message, contexte='info', lieu='carmel')

       ! Choix de la formulation.
       continuer = .TRUE.
       DO WHILE (continuer)
         ! DEBUG: continuation désactivée car non fiable
         !continuer = .FALSE.
         !write(stdout,*)  "ATTENTION! Une seule formulation peut etre calculee, car l'enchainement n'est pas fiable!"
         ! définition des noms usuels des formulation, qui auraient pu être écrasés par ailleurs
         call definirNomFormulation
         !
#if USE_MPI==1
         ! Avec MPI, affichage du menu de choix par l'hôte seulement (rang = 0)
         if (MPIrang == 0) then
#endif
         write(stdout,*)  ""
         write(stdout,*)  "Choix des formulations:"
         write(stdout,*)  "-----------------------"
         write(stdout,*)  "- 0: sortir"
         ! les noms de formulation ont été déterminés lors du choix du type de problème (definirTypeProbleme).
         write(stdout,'(A,A,A)') " - 1: resolution + post-traitement (formulation ", trim(adjustl(nomFormulation(kTOmega))), ')'
         write(stdout,'(A,A,A)') " - 2: resolution + post-traitement (formulation ", trim(adjustl(nomFormulation(kAPhi))), ')'
         write(stdout,'(A)') " - 3: post-traitement a posteriori, e.g., pertes"
         write(stdout,'(A)') " - 4: comparaison du resultat de post-traitement avec une solution de reference"
         WRITE(stdout,'(A)', ADVANCE = "NO") " - Votre choix: "
#if USE_MPI==1
         endif ! MPIrang = 0
#endif
         read(*,*,iostat=erreur) choix
         ! test integer value as input
         if (erreur /= 0) then
            write(stdout,*)  'ERREUR! Vous devez entrer une valeur entière. Refaites votre choix svp.'
            cycle
         endif

         ! définition du problème et de la formulation
         ! qui doit être réalisé avant le traitement des conditions de périodicité
         call definirprobleme(choix)

         ! fin du programme
         if ( choix == 0) goto 10

         ! initialisation du temps
         t = 0.0d0

        !
        ! En mode de redémarrage, vérification que le pas de temps demandé a bien été sauvegardé
        if (trim(adjustl(sav%type)) == 'redemarrage' .and. sav%itempsRedemarrage > 0 .and. &
           (choix == 1 .or. choix == 2)) then
            ! message utilisateur
            write(i2c,*) sav%itempsRedemarrage
            write(message,*) 'Redemarrage de la simulation au pas de temps ',trim(adjustl(i2c)),'.'
            call writeMessage(message,'warning','carmel')
            ! Nom du fichier solution sauvegardé au pas de temps précédent
            nom = modifierNomFichier(sav%nomSolution,modif='ajoutFormulation',it=sav%itempsRedemarrage-1)
            ! vérification de l'existence du fichier contenant la sauvegarde
            existe = .false.
            inquire(FILE=trim(adjustl(nom)),EXIST=existe)
            ! Erreur si le fichier n'existe pas
            if (.not.existe) call stopErreur(nom,'carmel',contexte=kErreurFichierAbsent,arret=.true.)
        endif
        !
        ! En mode de redémarrage automatique au dernier pas de temps sauvegardé, détermination du pas de temps
        if (trim(adjustl(sav%type)) == 'redemarrage' .and. sav%itempsRedemarrage == 0 .and. &
           (choix == 1 .or. choix == 2)) then
            write(message,*) "Redemarrage automatique : determination du dernier pas de temps sauvegarde..."
            call writeMessage(message,'warning','verifierParametrePrecision')
            if (debug) then
                write(message,*) &
                 "Parcours de tous les pas de temps a partir du premier pour trouver le dernier fichier de sauvegarde..."
                call writeMessage(message,'debug','carmel')
            endif
            ! Parcours de tous les pas de temps pour trouver la dernière solution disponible,
            ! selon que les fichiers de sauvegarde existent ou pas.
            do i=1,NBoucleTemps
                ! pas de temps précédent sauvegardé
                nom = modifierNomFichier(sav%nomSolution,modif='ajoutFormulation',it=i)
                ! vérification de l'existence du fichier contenant la sauvegarde
                existe = .false.
                inquire(FILE=trim(adjustl(nom)),EXIST=existe)
                ! Erreur si le fichier n'existe pas
                if (existe) then
                    sav%itempsRedemarrage = i+1
                    if (debug) then
                        write(message,*) "On trouve le fichier : ",trim(adjustl(nom))
                        call writeMessage(message,'debug','carmel')
                    endif
                else
                    if (debug) then
                        write(message,*) "Absence du fichier : ",trim(adjustl(nom))
                        call writeMessage(message,'debug','carmel')
                    endif
                endif
            end do
            ! Message selon la sauvegarde trouvée ou pas
            if (sav%itempsRedemarrage == 0) then
                write(message,*) 'Aucune sauvegarde de solution disponible. Redemarrage de la simulation au premier pas de temps.'
                sav%itempsRedemarrage = 1
            elseif (sav%itempsRedemarrage > NBoucleTemps) then
                ! message utilisateur
                write(i2c,*) NBoucleTemps
                write(message,*) "La simulation est complete, jusqu'au pas de temps ",trim(adjustl(i2c)),&
                                    '. Sortie du programme.'
                call writeMessage(message,'warning','carmel')
                ! sortie du programme proprement.
                goto 10
            else
                ! message utilisateur
                write(i2c,*) sav%itempsRedemarrage
                write(message,*) 'Redemarrage de la simulation au pas de temps ',trim(adjustl(i2c)),'.'
                call writeMessage(message,'warning','carmel')
            end if
        end if



         ! la formulation est connue
         ! Traitement des noeuds et aretes si condition de periodicite.
         CALL ConditionsPeriodicites(nombreConditionsPeriodicites,erreur)
         !
         call testErreur(erreur,&
            "test d'erreur apres appel a la routine ConditionsPeriodicites",'carmel',contexte=erreur,arret=.true.)

         ! Dernière vérification de la configuration avant résolution (on connaît la formulation et le type de problème)
         call writeMessage("Verification de la configuration pour le type de probleme choisi...",'info','carmel')
         call verifierConfiguration
         call writeMessage("... configuration verifiee (erreurs eventuelles non critiques).",contexte='ok',lieu='carmel')


         SELECT CASE(choix)
           CASE(1)
             ! Traitement des arêtes pour le regroupement des faces
             if (clJindn0Tmonogroupe) call verifinterface()
             ! résolution
             CALL resolutionHsOmega(erreur)
             !
             call testErreur(erreur,"test d'erreur apres appel a la routine resolutionHsOmega",'carmel'&
                             , contexte=erreur, arret=.true.)
             !
           CASE(2)
             ! résolution
             CALL resolutionA(erreur)
             !
             call testErreur(erreur,"test d'erreur apres appel a la routine resolutionA",'carmel',&
                             contexte=erreur, arret=.true.)
             !
           CASE(3,4)
             write(stdout,*)  "Quelle formulation ?"
             write(stdout,'(A)') " - 0: toutes les formulations"
             write(stdout,'(A,A)') " - 1: formulation ", trim(adjustl(nomFormulation(kTOmega)))
             write(stdout,'(A,A)') " - 2: formulation ", trim(adjustl(nomFormulation(kAPhi)))
             WRITE(stdout,'(A)', ADVANCE = "NO") " - Votre choix: "
             read(*,*,iostat=erreur) choixFormulation
             ! test integer value as input
             if (erreur /= 0) then
                write(stdout,*)  'ERREUR! Vous devez entrer une valeur entière. Refaites votre choix svp.'
                cycle
             endif
             !
             SELECT CASE(choixFormulation)
               ! toutes les formulations
               CASE(0)
                 formulation = 0
               CASE(1)
                 formulation = kTOmega
               CASE(2)
                 formulation = kAPhi
               CASE DEFAULT
                 write(stdout,*)  " - Votre choix ne correspond a aucune option"
             END SELECT
             !
! Choix 11 et 21 pour le développement de la refonte désactivés
!           CASE(11)
!
!             formulation = kTOmega
!
!             CALL resolution(erreur)
!
!           CASE(21)
!
!             formulation = kAPhi
!
!             CALL resolution(erreur)


           CASE DEFAULT
             write(stdout,*)  " - Votre choix ne correspond a aucune option"
           END SELECT
           ! choix 3 ou 4
           select case(choix)
           case(3)
                call postraitement
           case(4)
                call comparerPostraitementReference
           end select
       END DO

       ! libération de mémoire
10      deallocate(listeDt,stat=erreur)
        call testDestruction(erreur,'listeDt','carmel',global=.true.)
        !
        deallocate(listeTemps,stat=erreur)
        call testDestruction(erreur,'listeTemps','carmel',global=.true.)

        !
        ! destruction des tableaux globaux
        !

        ! destruction des tableaux de lecture des familles/groupes du maillage MED
        if (allocated(maillageMed%labelFamilles)) then
            deallocate(maillageMed%labelFamilles,stat=erreur)
            call testDestruction(erreur,'maillageMed%labelFamilles','carmel',global=.true.)
        end if
        !
        if (allocated(maillageMed%nbGroupesFamilles)) then
            deallocate(maillageMed%nbGroupesFamilles,stat=erreur)
            call testDestruction(erreur,'maillageMed%nbGroupesFamilles','carmel',global=.true.)
        end if

        ! destruction de la liste des noeuds et tableaux associés
        ! destruction de la connectivité noeuds/éléments
        ! message car cela peut être un peu long
        write(stdout,*) ''
        write(stdout,*) 'Destruction de la memoire de la connectivite entre noeuds et elements...'
        do i=1,nombreNoeuds
            deallocate(listeNoeuds(i)%listeIndicesElements,stat=erreur)
            ! conversion nombre de noeuds en chaîne
            write(message,*) nombreNoeuds
            call testDestruction(erreur,'listeNoeuds(1:'//trim(adjustl(message))//')%listeIndicesElements','carmel',&
                                    identifiant='listeNoeuds(1:'//trim(adjustl(message))//')%listeIndicesElements')
        enddo
        deallocate(listeNoeuds,stat=erreur)
        call testDestruction(erreur,'listeNoeuds','carmel',global=.true.)

        deallocate(listeElements,stat=erreur)
        call testDestruction(erreur,'listeElements','carmel',global=.true.)

        deallocate(listeAretes,stat=erreur)
        call testDestruction(erreur,'listeAretes','carmel',global=.true.)

        deallocate(listeFacettes,stat=erreur)
        call testDestruction(erreur,'listeFacettes','carmel',global=.true.)

        call detruireSystemeMorse(systeme,'systeme',initialiser=.true.)

        if (allocated(postraitementdonne%pertes%loci_elements)) then
            deallocate(postraitementdonne%pertes%loci_elements,stat=erreur)
            call testDestruction(erreur,'postraitementdonne%pertes%loci_elements','carmel',global=.true.)
        endif
        !
        if (allocated(postraitementdonne%pertes%fft_elements)) then
            deallocate(postraitementdonne%pertes%fft_elements,stat=erreur)
            call testDestruction(erreur,'postraitementdonne%pertes%fft_elements','carmel',global=.true.)
        endif
        !
        if (allocated(postraitementdonne%carteChampsElements)) then
            deallocate(postraitementdonne%carteChampsElements,stat=erreur)
            call testDestruction(erreur,'postraitementdonne%carteChampsElements','carmel',global=.true.)
        endif
        ! Destruction des tableaux de la refonte
        if (allocated(RotnuRot)) then
            do i=1,nombreMilieux
                ! conversion de l'indice de milieu en chaîne de caractères, pour nommer les tableaux
                write(entierTexte,*) i
                ! destruction si tableaux créés
                if (allocated(RotnuRot(i)%A)) then
                    deallocate(RotnuRot(i)%A,stat=erreur)
                    call testDestruction(erreur,&
                        'RotnuRot('//trim(adjustl(entierTexte))//')%A','carmel',global=.true.)
                endif
                if (allocated(RotnuRot(i)%iA)) then
                    deallocate(RotnuRot(i)%iA,stat=erreur)
                    call testDestruction(erreur,&
                        'RotnuRot('//trim(adjustl(entierTexte))//')%iA','carmel',global=.true.)
                endif
                if (allocated(RotnuRot(i)%jA)) then
                    deallocate(RotnuRot(i)%jA,stat=erreur)
                    call testDestruction(erreur,&
                        'RotnuRot('//trim(adjustl(entierTexte))//')%jA','carmel',global=.true.)
                endif
            enddo
            deallocate(RotnuRot,stat=erreur)
            call testDestruction(erreur,'RotnuRot','carmel',global=.true.)
        endif

        ! Destruction du mouvement (tableaux dynamiques de chaque mouvement)
        do i=1,nombreMouvements
            !if (associated(listeMouvements(i)%IndiceAreteApermute)) then
            if (allocated(listeMouvements(i)%IndiceAreteApermute)) then
                write(entierTexte,*) i
                call testDestruction(erreur,&
                    'listeMouvements('//trim(adjustl(entierTexte))//')%IndiceAreteApermute','carmel',global=.true.)
            endif
            if (allocated(listeMouvements(i)%IndiceNoeudApermute)) then
                write(entierTexte,*) i
                call testDestruction(erreur,&
                    'listeMouvements('//trim(adjustl(entierTexte))//')%IndiceNoeudApermute','carmel',global=.true.)
            endif
        enddo

        ! Destruction des points explorateurs
        do i=1,kNombreMaximumGroupesPointsExplorateursChamp
            if (pointsExplorateursChamps(i)%nb > 0) then
                ! indice de groupe -> chaîne pour définir le nom du tableau à sauvegarder dans les routines de gestion mémoire
                write(entierTexte,*) i
                !
                if (allocated(pointsExplorateursChamps(i)%coord)) then
                    deallocate(pointsExplorateursChamps(i)%coord,stat=erreur)
                    call testDestruction(erreur,'pointsExplorateursChamps('//trim(adjustl(entierTexte))//')%coord',&
                                         'carmel',global=.true.)
                endif
                !
                if (allocated(pointsExplorateursChamps(i)%coordRef)) then
                    deallocate(pointsExplorateursChamps(i)%coordRef,stat=erreur)
                    call testDestruction(erreur,'pointsExplorateursChamps('//trim(adjustl(entierTexte))//')%coordRef',&
                                         'carmel',global=.true.)
                endif
                !
                if (allocated(pointsExplorateursChamps(i)%elements)) then
                    deallocate(pointsExplorateursChamps(i)%elements,stat=erreur)
                    call testDestruction(erreur,'pointsExplorateursChamps('//trim(adjustl(entierTexte))//')%elements',&
                                         'carmel',global=.true.)
                endif
                !
                if (allocated(pointsExplorateursChamps(i)%ielt)) then
                    deallocate(pointsExplorateursChamps(i)%ielt,stat=erreur)
                    call testDestruction(erreur,'pointsExplorateursChamps('//trim(adjustl(entierTexte))//')%ielt',&
                                         'carmel',global=.true.)
                endif
                !
                if (allocated(pointsExplorateursChamps(i)%ieltMilieux)) then
                    deallocate(pointsExplorateursChamps(i)%ieltMilieux,stat=erreur)
                    call testDestruction(erreur,'pointsExplorateursChamps('//trim(adjustl(entierTexte))//')%ieltMilieux',&
                                         'carmel',global=.true.)
                endif
                !
            end if
        end do

        !
        if (allocated(listeElementsNL)) then
            deallocate(listeElementsNL,stat=erreur)
            call testDestruction(erreur,'listeElementsNL','carmel',global=.true.)
        endif
        !
        if (allocated(listeElementsL)) then
            deallocate(listeElementsL,stat=erreur)
            call testDestruction(erreur,'listeElementsL','carmel',global=.true.)
        endif


        ! destruction des tableaux nécessaires aux inducteurs définis par des lignes directrices, si définis
        do i=1,nombreMilieux
            if (listeMilieux(i)%JligneDirectrice /= '')  then
                if (allocated(listeMilieux(i)%noeudsJ)) then
                    write(entierTexte,*) i
                    deallocate(listeMilieux(i)%noeudsJ,stat=erreur)
                    call testDestruction(erreur,'listeMilieux('//trim(adjustl(entierTexte))//')%noeudsJ','carmel',global=.true.)
                end if
                !
                if (allocated(listeMilieux(i)%aretesJ)) then
                    write(entierTexte,*) i
                    deallocate(listeMilieux(i)%aretesJ,stat=erreur)
                    call testDestruction(erreur,'listeMilieux('//trim(adjustl(entierTexte))//')%aretesJ','carmel',global=.true.)
                end if
            end if
        end do

        ! lecture du pic mémoire
        picMemoireLu = read_vmpeak() * 1024**2 ! conversion en octets du résultat en Mo.
        ! affichage des tableaux dynamiques restants en memoire, et du pic mémoire
        if (debug) call bilanMemoire
        !
        ! destruction du tableau de gestion de la mémoire
        if (allocated(memoire)) then
            deallocate(memoire,stat=erreur)
            ! pas de test de destruction parce plus de gestion mémoire disponible.
            ! On réalise quand même un test d'erreur
            call testErreur(erreur,'liberation du tableau memoire','carmel')
        endif
        !
        ! affichage, en mode debug, des descripteurs de fichiers non encore détruits, si existants.
        if (debug) call bilanDescripteur

#if USE_MPI==1
        ! finalisation de MPI
        call MPI_FINALIZE(erreur)
#endif
        !
     END PROGRAM carmel
