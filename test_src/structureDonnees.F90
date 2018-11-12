! +----------------------------------------------------------------------------+
! | FICHIER   : structureDonnees.F90                                           |
! | MODULE    : structureDonnees.                                              |
! | LANGAGE   : fortran 90.                                                    |
! | OBJET     : structure de donnees globale.                                  |
! | AUTEURS   : - Yvonnick LE MENACH (L2EP).                                   |
! |           : - Georges MARQUES (L2EP).                                      |
! |           : - Emmanuel CAGNIOT (LIFL).                                     |
! |           : - Loic CHEVALLIER (L2EP).                                      |
! |           : - Olivier BOITEAU (EDF).                                       |
! |           : - Julien KORECKI (L2EP).                                       |
! |           : - Jean-Pierre DUCREUX (EDF).                                   |
! | CREATION  : xx.xx.xx.                                                      |
! | REVISION  : xx.xx.xx.                                                      |
! | REMARQUES : neant.                                                         |
! +----------------------------------------------------------------------------
     MODULE structureDonnees
     !$ use OMP_LIB
#if USE_MPI==1
     use mpi
#endif

     IMPLICIT NONE

#if USE_MPI==1
     !include 'mpif.h'
#endif

#if USE_MUMPS==1
     ! Structures nécessaires à l'utilisation de MUMPS
!     include 'smumps_struc.h'
!     include 'dmumps_struc.h'
#endif

       ! +---------------------------------------------------------------------+
       ! | Types et constantes, publiques et privees.                          |
       ! +---------------------------------------------------------------------+

       ! Constantes relatives a l'identification de l'application.
       !> Nom du programme, affiché au début de l'éxécution
       CHARACTER(LEN = 100), PARAMETER :: kNomCode = "code_Carmel : elements finis 3D en electromagnetisme"
       !> Auteurs
       CHARACTER(LEN = 300), PARAMETER :: kAuteurs = "Auteurs : Y. Le Menach (L2EP), G. Marques (L2EP),"//&
        " T. Henneron (L2EP), E. Cagniot (LIFL), L. Chevallier (L2EP), J. Korecki (L2EP), O. Boiteau (EDF),"//&
        " J.-P. Ducreux (EDF), L. Montier (LAMEL), A. Pierquin (LAMEL)."
       !> Mentions Légales
       CHARACTER(LEN=80), PARAMETER :: kMentionsLegales = "(c) LAMEL/L2EP/EDF, 2017"
       ! Versions
       !> version du programme
       CHARACTER(LEN = 10), PARAMETER :: kVersion = "1.14.0"
       !> Date de dernière modification du programme
       CHARACTER(LEN = 60), PARAMETER :: kDate = "2017/07/19"
       !> version de la structure des données
       INTEGER, PARAMETER :: versionStructureDonnees = 19
       !> version de la configuration,
       !! séparé de la version de la structure des données car cette dernière peut changer
       !! alors que la configuration demeure inchangée.
       INTEGER :: versionConfiguration = 17
       !> version de la solution sauvegardée éventuellement.
       !! - inexistant ou 0 : inconnues sauvegardées seulement (nombre d'inconnues et systeme%X),
       !!                     les autres grandeurs globales étant lues à partir des fichiers *.don.
       !! - 1 : inconnues, auxquelles on rajoute les courant et flux de tous les inducteurs.
       INTEGER :: kVersionSolution = 1

       !> Sortie standart d'écriture (6 = affichage écran pour rang 0 ou séquentiel par défaut, fort.rang pour les autres)
       !! Sa valeur est définie en tout début de programme
       integer :: stdout

       !> Nombre de taches paralleles OpenMP (threads), si actif, ou 1 sinon
       !! Grandeur globale car nécessaire même sans parallélisme, e.g.,
       !! pour décider si l'assemblage des termes matrice+source se fait en parallèle ou pas
       integer :: nbthreads

       !> Nom de la machine sur laquelle le calcul est lancé, obtenu par la fonction Fortran intrinsèque HOSTNM()
       !character(len=50) :: hostname

       !> Identifiant Linux (PID) du processus, obtenu par la fonction Fortran intrinsèque GETPID()
       !integer :: pid

#if USE_MPI==1
       ! Variables dédiées à MPI
       !> Nombre de processus MPI
       integer :: MPIprocs
       !> Rang du processus MPI courant (commence à 0 pour l'hôte)
       integer :: MPIrang
       !> Nom de la machine sur lequel tourne le processus MPI courant
       character(len=MPI_MAX_PROCESSOR_NAME) :: MPIhostname
       !> Longueur réelle du nom de la machine sur lequel tourne le processus MPI courant
       integer :: MPIhostnamelength
#endif

       !> Identification du problème traité, géométrie, modèle, etc.
       CHARACTER(LEN = 200) :: identification
       ! Constantes relatives aux fichiers manipules.
       !> Nom du fichier de maillage (suffixe .med ou .unv), défini dans le programme efl2ep.f90
       CHARACTER(LEN = 50) :: fichierMaillage
       !> Nom du fichier de post-traitement pour le format MED, défini automatiquement dans efl2ep.f90
       CHARACTER(LEN = 50)  :: fichierPostpro
       !> Nom du fichier contenant la description du circuit (netlist)
       CHARACTER(LEN = 50)  :: kFichierCircuit

       !> Indicateur de groupes de maillage lus (.true.) ou pas (.false.)
       !! utilisé dans la routine indiceGroupe et ses déclinaisons selon le format de maillage
       logical :: groupesMaillageLus
       ! Maillage au format UNV (I-Deas)
       ! Numeros des data set relatifs a la mesure utilisee, aux noeuds et aux
       ! elements.
       INTEGER, PARAMETER :: kNumeroDataSetMesure   = 164
       INTEGER, PARAMETER :: kNumeroDataSetNoeuds   = 2411
       INTEGER, PARAMETER :: kNumeroDataSetElements = 2412
       INTEGER, PARAMETER :: kNumeroDataSetGroupes  = 2477
       INTEGER, PARAMETER :: kNumeroDataSetGroupesSalome  = 2467
       !> Numéro de dataset UNV contenant un champ vectoriel ou scalaire
       INTEGER, PARAMETER :: kNumeroDataSetChamp  = 2414

       ! Signature litterale et numerique de debut et de fin d'un data set.
       CHARACTER(LEN=6), PARAMETER :: kSignatureLitteraleDataSet =&
        "    -1"
        INTEGER, PARAMETER :: kSignatureNumeriqueDataSet = -1

       ! Choix de la formulation
       !
       !> Formulation T, Omega ou T-Omega
       INTEGER, PARAMETER :: kTOmega = 1
       !> Formulation A, A-Phi, ou Phi
       INTEGER, PARAMETER :: kAPhi = 2
       !> liste des noms usuels de chaque formulation pour un problème magnétodynamique, pour affichage
       character(len=7), dimension(2), parameter :: kNomFormulationMagnetodynamique = (/'T-Omega','A-Phi  '/)
       !> liste des noms usuels de chaque formulation pour un problème magnétodynamique, utilisée dans un nom de fichier
       character(len=6), dimension(2), parameter :: kNomFormulationMagnetodynamiqueFichier = (/'TOmega','APhi  '/)
       !> liste des noms usuels de chaque formulation pour un problème magnétostatique, pour affichage ou utilisation dans un nom de fichier
       character(len=5), dimension(2), parameter :: kNomFormulationMagnetostatique = (/'Omega','A    '/)
       !> liste des noms usuels de chaque formulation pour un problème électrocinétique, pour affichage ou utilisation dans un nom de fichier
       character(len=3), dimension(2), parameter :: kNomFormulationElectrocinetique = (/'T  ','Phi'/)
       !> Nom de la formulation courante pour affichage (usage interne)
       character(len=7), dimension(2) :: nomFormulation
       !> Nom de la formulation courante  utilisée dans un nom de fichier (usage interne)
       character(len=7), dimension(2) :: nomFormulationFichier

       ! Choix du type de problème
       !
       INTEGER, PARAMETER ::  knombreprobleme = 1
       !> Problème magnétostatique
       INTEGER, PARAMETER :: kMagnetostatique = 1
       !> Problème électrocinétique
       INTEGER, PARAMETER :: kElectrocinetique = 2
       !> Problème magnétodynamique
       INTEGER, PARAMETER :: kMagnetodynamique = 3

       !> Nombre maximum de milieux utilisés.
       INTEGER, PARAMETER :: kNombreMaximumMilieux  = 150

       INTEGER, PARAMETER :: krapportnoeudarete     = 7
       !> Ce nombre permet de calculer les nombres de facettes pour les maillages non simplement connexes et nonconformes
       INTEGER, PARAMETER :: kmarge                 = 10

       ! Conditions aux limites.
       INTEGER, PARAMETER :: kNombreMaximumConditionsLimites = 100
       INTEGER, PARAMETER :: kPasDeCondition                 = 0
       INTEGER, PARAMETER :: kConditionLimite_A              = 1
       INTEGER, PARAMETER :: kConditionLimite_Omega          = 2
       INTEGER, PARAMETER :: kConditionLimite_Hs             = 3
       INTEGER, PARAMETER :: kConditionLimite_T              = 4
       INTEGER, PARAMETER :: kConditionLimite_Phi            = 5
       INTEGER, PARAMETER :: kConditionLimite_J              = 6

       ! Groupes, macro-groupes, inducteurs, aimants, spire exploratrice.
       !> Nombre maximal de groupes de noeuds et d'éléments, i.e., de leur somme.
       INTEGER, PARAMETER :: kNombreMaximumGroupes           = 150
       INTEGER, PARAMETER :: kNombreMaximumMGroupesNoeuds    = 100
       INTEGER, PARAMETER :: kNombreMaximumMGroupesAretes    = 100
       INTEGER, PARAMETER :: kNombreMaximumMGroupesFacettes  = 100
       INTEGER, PARAMETER :: kNombreMaximumMGroupesMilieux   = 100
       INTEGER, PARAMETER :: kNombreMaximumInducteurs        = 30
       INTEGER, PARAMETER :: kNombreMaximumSourceMagn        = 10
       INTEGER, PARAMETER :: kNombreMaximumAimants           = 50
       INTEGER, PARAMETER :: knombreMaximumAretesSpire       = 100
       INTEGER, PARAMETER :: kNombreMaximumSpireExploratrice = 100
       INTEGER, PARAMETER :: kNombreMaximumGroupeElementFlux = 100
       INTEGER, PARAMETER :: kNombreMaximumCoupure           = 100
       INTEGER, PARAMETER :: knombreMaximumEleGeoCoupure     = 1000
       INTEGER, PARAMETER :: knombreMaximumElementCoupure    = 2000
       INTEGER, PARAMETER :: knombreMaximumconditionChamp    = 100
       INTEGER, PARAMETER :: knombreMaximumconditionPeridicite = 10
       INTEGER, PARAMETER :: knombreMaximumElementsPeriodique = 1510
       INTEGER, PARAMETER :: knombreMaximumElementMouvement   = 15000
       INTEGER, PARAMETER :: knombreMaximumMouvement          = 1
       INTEGER, PARAMETER :: knombreMaximumNoeudMouvement     = 1500
       INTEGER, PARAMETER :: knombreMaximumAreteMouvement     = 5000
       INTEGER, PARAMETER :: kNombreMaximumForceCouple        = 1
       INTEGER, PARAMETER :: knombreMaximumPotFlottant        = 1
       ! contantes utilisées pour le couplage circuit avec qucs T1.11.X, T1.12.X
       INTEGER, PARAMETER :: kNombreMaximumDipole     = 100
       INTEGER, PARAMETER :: kNombreMaximumNoeudsCirc = 100
       INTEGER, PARAMETER :: kNombreMaximumArbreCirc  = 100 ! max arbre et ou max boucles de courants
       !> Nombre maximum d'éléments explorateurs du champ lorsque on les choisit à la main
       INTEGER, PARAMETER :: kNombreMaximumElementsExplorateurs = 100
       !> Nombre maximal de groupes de points explorateurs des champs,
       !! ce qui permet, e.g., de créer plusieurs lignes, plans, arcs ou tracés de coupe
       integer, parameter :: kNombreMaximumGroupesPointsExplorateursChamp = 100


       !> Nombre initial d'éléments maximum par noeud.
       !! Cela permet d'initialiser une taille dynamique de tableau.
       !! Cette taille peut ensuite évoluer automatiquement,
       !! i.e., croître de double en double, selon chaque noeud.
       !! Ce paramètre est désactivé pour le moment car non utilisé
       !integer, parameter :: kNombreInitialDegreNoeudElement = 300

       !> Pour preciser qu'un champ a des noeuds comme support
       integer, parameter :: kNoeud = 1
       !> Pour preciser qu'un champ a des éléments comme support
       integer, parameter :: kElement = 2

       !> Nombre et indices des elements de reference dans la liste correspondante.
       !INTEGER, PARAMETER :: kNombreElementsReference = 6
       INTEGER, PARAMETER :: kNombreElementsReference = 4
       INTEGER, PARAMETER :: kIndiceTetraedre         = 1
       INTEGER, PARAMETER :: kIndicePrisme            = 2
       INTEGER, PARAMETER :: kIndicePyramide          = 3
       INTEGER, PARAMETER :: kIndiceHexaedre          = 4
       !INTEGER, PARAMETER :: kIndiceTriangle          = 5
       !INTEGER, PARAMETER :: kIndiceQuadrangle        = 6

       !> Types des elements de reference dans la liste correspondante, sous forme de tableau
       integer, dimension(kNombreElementsReference) :: kTypeElement =&
       (/kIndiceTetraedre, kIndicePrisme, kIndicePyramide, kIndiceHexaedre/)
       !integer, dimension(kNombreElementsReference) :: kTypeElement = (/kIndiceTetraedre, kIndicePrisme, kIndiceHexaedre/)
       ! Pour plus tard, car les groupes d'arêtes ou de faces (triangle, quadrangle) posent problème avec MED 3
       !integer, dimension(kNombreElementsReference) :: kTypeElement =&
       !(/kIndiceTriangle, kIndiceQuadrangle, kIndiceTetraedre, kIndicePrisme, kIndicePyramide, kIndiceHexaedre/)

       !> Nom des types des elements de reference
       character(len=9), dimension(kNombreElementsReference) :: kNomTypeElement = &
                                                                 (/'Tetraedre',&
                                                                   'Prisme   ',&
                                                                   'Pyramide ',&
                                                                   'Hexaedre '/)
!       character(len=10), dimension(kNombreElementsReference) :: kNomTypeElement = &
!                                                                 (/'Triangle  ',&
!                                                                   'Quadrangle',&
!                                                                   'Tetraedre ',&
!                                                                   'Prisme    ',&
!                                                                   'Pyramide  ',&
!                                                                   'Hexaedre  '/)

       !> Caracteristiques des elements de reference.
       INTEGER, PARAMETER :: kNombreNoeudsTriangle    = 3
       INTEGER, PARAMETER :: kNombreNoeudsQuadrangle  = 4
       INTEGER, PARAMETER :: kNombreNoeudsTetraedre   = 4
       INTEGER, PARAMETER :: kNombreNoeudsPrisme      = 6
       INTEGER, PARAMETER :: kNombreNoeudsPyramide    = 5
       INTEGER, PARAMETER :: kNombreNoeudsHexaedre    = 8
       INTEGER, PARAMETER :: kNombreAretesTriangle    = 3
       INTEGER, PARAMETER :: kNombreAretesQuadrangle  = 4
       INTEGER, PARAMETER :: kNombreAretesTetraedre   = 6
       INTEGER, PARAMETER :: kNombreAretesPrisme      = 9
       INTEGER, PARAMETER :: kNombreAretesPyramide    = 8
       INTEGER, PARAMETEr :: kNombreAretesHexaedre    = 12
       INTEGER, PARAMETER :: kNombreFacettesTriangle  = 1
       INTEGER, PARAMETER :: kNombreFacettesQuadrangle= 1
       INTEGER, PARAMETER :: kNombreFacettesTetraedre = 4
       INTEGER, PARAMETER :: kNombreFacettesPrisme    = 5
       INTEGER, PARAMETER :: kNombreFacettesPyramide  = 5
       INTEGER, PARAMETER :: kNombreFacettesHexaedre  = 6
       !> Nombre de noeuds des elements de reference dans la liste correspondante, sous forme de tableau
       integer, dimension(kNombreElementsReference) :: kTypeElementNbNoeuds = (/kNombreNoeudsTetraedre, kNombreNoeudsPrisme,&
        kNombreNoeudsPyramide, kNombreNoeudsHexaedre/)
!       integer, dimension(kNombreElementsReference) :: kTypeElementNbNoeuds = (/kNombreNoeudsTetraedre, kNombreNoeudsPrisme,&
!        kNombreNoeudsPyramide, kNombreNoeudsHexaedre, kNombreNoeudsTriangle, kNombreNoeudsQuadrangle/)
       ! Caracteristiques maximum d'un element.
       INTEGER, PARAMETER :: kNombreMaximumNoeudsElement = kNombreNoeudsHexaedre
       INTEGER, PARAMETER :: kNombreMaximumAretesElement = kNombreAretesHexaedre
       INTEGER, PARAMETER :: kNombreMaximumFacettesElement = kNombreFacettesHexaedre
       INTEGER, PARAMETER :: kNombreMaximumNoeudsFacette  = 4
       INTEGER, PARAMETER :: kNombreMaximumAretesFacette  = 4
       INTEGER, PARAMETER :: kNombreMaximumAretesFacettes = kNombreMaximumFacettesElement * kNombreMaximumAretesFacette
       INTEGER, PARAMETER :: kNombreMaximumInconnuesElement = &
                               kNombreMaximumAretesElement + &
                               kNombreMaximumNoeudsElement + &
                               kNombreMaximumInducteurs   + &
                               kNombreMaximumSourceMagn

       !> Largeur maximale de la bande de la matrice creuse A,
       !! qui sert à réaliser une première estimation de la taille de la matrice A
       !! à partir du nombre d'inconnues calculé auparavant.
       !! Cette largeur de bande est fixée à 21, maximum atteint pour un maillage d'hexaèdres.
       INTEGER, PARAMETER :: klargeurdebande   = 21! pour Hexaedre

       ! Codes d'erreurs.
       !> Constante indiquant un paramètre de valeur invalide, e.g., hors portée définie.
       !! Elle est utilisée notamment comme retour d'erreur lors de la recherche sur un tableau de chaînes ou d'entiers.
       integer, parameter :: kErreurInvalide = -1
       !> Erreur inconnue, en doublon de kErreurInvalide ???
       INTEGER, PARAMETER :: kErreurInconnue             = -1
       INTEGER, PARAMETER :: kAucuneErreur               = 0
       INTEGER, PARAMETER :: kErreurOuvertureFichier     = 1
       INTEGER, PARAMETER :: kErreurLectureFichier       = 2
       INTEGER, PARAMETER :: kErreurFinFichierInattendue = 3
       INTEGER, PARAMETER :: kErreurNombreNoeudsElement  = 4
       !> On signale une erreur de programmation, e.g., incoherence entre tailles de tableaux
       INTEGER, PARAMETER :: kErreurProgrammation  = 5
       !> Le fichier contenant le maillage est absent.
       !! C'est tellement important que le message d'erreur doit être personnalisé,
       !! par rapport à un fichier quelconque absent, traité par un autre code d'erreur
       INTEGER, PARAMETER :: kErreurFichierMaillageAbsent = 6
       !> On signale une erreur de mémoire dynamique allocate
       INTEGER, PARAMETER :: kErreurAllocate = 7
       INTEGER, PARAMETER :: kErreurNombreGroupes        = 10
       INTEGER, PARAMETER :: kErreurCoArbreFacettes      = 11
       INTEGER, PARAMETER :: kErreurArbreAretes          = 12
       INTEGER, PARAMETER :: kErreurPreconditionneur     = 15
       INTEGER, PARAMETER :: kErreurConvergenceGCP       = 16
       INTEGER, PARAMETER :: kErreurNombreEleGeoCoupure  = 19
       INTEGER, PARAMETER :: kErreurNombreElementCoupure = 20
       INTEGER, PARAMETER :: kErreurGroupeNonDistinct    = 21
       INTEGER, PARAMETER :: kErreurNombreMilieux        = 23
       INTEGER, PARAMETER :: kErreurNoeudNombreMouvement = 31
       INTEGER, PARAMETER :: kErreurNombrePotentielFlottant = 35
       INTEGER, PARAMETER :: kErreurReacPrecond = 36
       !> Le fichier est absent
       INTEGER, PARAMETER :: kErreurFichierAbsent = 37
       INTEGER, PARAMETER :: kErreurFermetureFichier = 38
       INTEGER, PARAMETER :: kErreurEcritureFichier = 39
       !> Erreur de configuration du code ou du modèle
       integer, parameter :: kErreurConfiguration = 40
       !> Erreur liée à une taille dépassant les tailles maximales définies dans le code
       integer, parameter :: kErreurTaille = 41

       !> Nom du fichier, dit de log, dans lequel les messages d'erreur et d'avertissement sont écrits
       !! Ces messages doivent passer pas la routine writeMessage
       character(len=7), parameter :: kFichierLog = 'log.txt'

       ! Points de Gauss.
       INTEGER, PARAMETER :: kNombreMaximumPointsGauss = 15

       ! Formats de sortie pour les cartes.
       ! Ils servent aussi pour les formats d'entrée de maillage
       !> Nombre maximal de formats, d'entrée ou de sortie
       INTEGER, PARAMETER :: kNombreMaximumFormats = 5
       !> Format UNV (I-Deas)
       INTEGER, PARAMETER :: kUNV = 1
       !> Format CSV : données en colonnes, séparées par des espaces blancs, commentaires précédés par le caractère #
       INTEGER, PARAMETER :: kCSV = 2
       !> Format MED (SALOME)
       INTEGER, PARAMETER :: kMED = 3
       !> Format VTK (pour Windows...)
       INTEGER, PARAMETER :: kVTK = 4
       !> Format SAV (binaire) interne à code_Carmel
       INTEGER, PARAMETER :: kSAV = 5
       !> Noms des formats
       character(len=3), dimension(5) :: kNomFormats = (/'UNV','CSV','MED','VTK','SAV'/)

       ! Types de champs de sortie pour les cartes.
       !> Nombre maximal de champs définis ci-après
       INTEGER, PARAMETER :: kNombreMaximumChamps = 19
       !> champ vectoriel de type N (unités: 1/m2)
       INTEGER, PARAMETER :: kChampN = 1
       !> champ vectoriel de type rot K (unités: 1/m2)
       INTEGER, PARAMETER :: kChampRotK = 2
       !> champ vectoriel de type K (unités: 1/m)
       INTEGER, PARAMETER :: kChampK = 3
       !> champ vectoriel de type induction B (unités: T)
       INTEGER, PARAMETER :: kChampB = 4
       !> champ vectoriel de type courant induit J (unités: A/m2)
       INTEGER, PARAMETER :: kChampJ = 5
       !> champ vectoriel de type densité de force (unités: N/m2 ?)
       INTEGER, PARAMETER :: kChampForce = 6
       !> champ vectoriel de type magnétique H (unités: A/m)
       INTEGER, PARAMETER :: kChampH = 7
       !> champ vectoriel de type densité volumique de pertes Joules pour un temps donné (unités: W/m^3)
       INTEGER, PARAMETER :: kChampPerteJouleVolumique = 8
       !> champ scalaire aux éléments de type fréquence (unités: Hz)
       INTEGER, PARAMETER :: kChampFrequence = 9
       !> champ scalaire de type densité volumique de pertes (Fer, Cuivre, Fer conducteur) (unités: W/m^2)
       INTEGER, PARAMETER :: kChampPerteVolumique = 10
       !> champ scalaire de type densité massique de pertes  (Fer, Cuivre, Fer conducteur) (unités: W/kg)
       INTEGER, PARAMETER :: kChampPerteMassique = 11
       !> champ libre vectoriel aux éléments, pour utiliser les routines d'écriture/lecture hors champs déjà définis
       INTEGER, PARAMETER :: kChampVecteurElements = 12
       !> champ libre vectoriel aux noeuds, pour utiliser les routines d'écriture/lecture hors champs déjà définis
       INTEGER, PARAMETER :: kChampVecteurNoeuds = 13
       !> champ libre scalaire aux éléments, pour utiliser les routines d'écriture/lecture hors champs déjà définis
       INTEGER, PARAMETER :: kChampScalaireElements = 14
       !> champ libre scalaire aux noeuds, pour utiliser les routines d'écriture/lecture hors champs déjà définis
       INTEGER, PARAMETER :: kChampScalaireNoeuds = 15
       !> champ vectoriel indiquant l'axe normal et normalisé au plan de rotation du champ B, e.g., lors du calcul des pertes
       INTEGER, PARAMETER :: kChampAxeNormalRotationB = 16
       !> champ scalaire aux éléments, rapport entre les pertes sur l'axe mineur (champ B) et les pertes totales sur les 2 axes (sans unités)
       INTEGER, PARAMETER :: kChampPerteInfluenceAxeMineur = 17
       INTEGER, PARAMETER :: kChampA = 18
       !> champ scalaire de type potentiel Omega (unités: A)
       INTEGER, PARAMETER :: kChampOmega = 19

       !> Liste des noms de champ hors formulation
       character(len=23), dimension(kNombreMaximumChamps), parameter :: kChampNoms = &
        (/'N                      ',&
          'rotK                   ',&
          'K                      ',&
          'B                      ',&
          'Jinduit                ',&
          'forceTV                ',&
          'H                      ',&
          'PerteJouleVolumique    ',&
          'Frequence              ',&
          'PerteVolumique         ',&
          'PerteMassique          ',&
          'ChampVecteurElements   ',&
          'ChampVecteurNoeuds     ',&
          'ChampScalaireElements  ',&
          'ChampScalaireNoeuds    ',&
          'AxeNormalRotationB     ',&
          'PerteInfluenceAxeMineur',&
          'A                      ',&
          'Omega                  '/)
       !> Liste des noms de champ pour la formulation T, Omega ou T-Omega
       character(len=31), dimension(kNombreMaximumChamps), parameter :: kChampNomsTOmega = &
        (/'N                              ',&
          'rotK                           ',&
          'K                              ',&
          'B_HsOmega                      ',&
          'JinduitTOm                     ',&
          'forceTV_HsOm                   ',&
          'H_HsOmega                      ',&
          'PerteJouleVolumique_HsOm       ',&
          'Frequence_HsOmega              ',&
          'PerteVolumique_HsOmega         ',&
          'PerteMassique_HsOmega          ',&
          'ChampVecteurElements_HsOmega   ',&
          'ChampVecteurNoeuds_HsOmega     ',&
          'ChampScalaireElements_HsOmega  ',&
          'ChampScalaireNoeuds_HsOmega    ',&
          'AxeNormalRotationB_HsOmega     ',&
          'PerteInfluenceAxeMineur_HsOmega',&
          'A_HsOmega                      ',&
          'Omega_HsOmega                  '/)

       !> Liste des noms de champ pour la formulation A, Phi ou A-Phi
       character(len=26), dimension(kNombreMaximumChamps), parameter :: kChampNomsAPhi = &
        (/'N                         ',&
          'rotK                      ',&
          'K                         ',&
          'B_A                       ',&
          'JinduitAPhi               ',&
          'forceTV_A                 ',&
          'H_A                       ',&
          'PerteJouleVolumique_A     ',&
          'Frequence_A               ',&
          'PerteVolumique_A          ',&
          'PerteMassique_A           ',&
          'ChampVecteurElements_APhi ',&
          'ChampVecteurNoeuds_APhi   ',&
          'ChampScalaireElements_APhi',&
          'ChampScalaireNoeuds_APhi  ',&
          'AxeNormalRotationB_A      ',&
          'PerteInfluenceAxeMineur_A ',&
          'A_A                       ',&
          'Omega_A                   '/)

       !> Liste des description de champ
       character(len=66), dimension(kNombreMaximumChamps), parameter :: kChampDescriptions = &
        (/'support de courant (toute formulation)                            ',&
          'support de courant (formulation Omega ou T-Omega)                 ',&
          'support de courant, i.e., N = rot K (formulation Omega ou T-Omega)',&
          'induction magnetique                                              ',&
          'densite surfacique de courant induit                              ',&
          'densite surfacique de force                                       ',&
          'champ magnetique                                                  ',&
          'densite volumique de pertes Joule                                 ',&
          'frequence dominante des pertes Fer, apres analyse FFT             ',&
          'densite volumique de pertes Fer                                   ',&
          'densite massique de pertes Fer                                    ',&
          'champ libre vectoriel aux elements                                ',&
          'champ libre vectoriel aux noeuds                                  ',&
          'champ libre scalaire aux elements                                 ',&
          'champ libre scalaire aux noeuds                                   ',&
          'axe normal et normalise du plan de rotation du champ B            ',&
          'rapport des pertes sur axe mineur et pertes totales sur les 2 axes',&
          'potentiel A                                                       ',&
          'potentiel Omega                                                   '/)

       !> Liste des unités de champ
       character(len=4), dimension(kNombreMaximumChamps), parameter :: kChampUnites = &
        (/'1/m2',&
          '1/m2',&
          '1/m ',&
          'T   ',&
          'A/m2',&
          'N/m2',&
          'A/m ',&
          'W/m3',&
          'Hz  ',&
          'W/m3',&
          'W/kg',&
          '    ',&
          '    ',&
          '    ',&
          '    ',&
          '    ',&
          'T.m ',&
          'A   ',&
          '    '/)

       !> Liste de la dépendance en temps (.true.) ou non (.false.) des champs
       logical, dimension(kNombreMaximumChamps), parameter :: kChampTemps = &
        (/.false.,&
          .false.,&
          .false.,&
          .true.,&
          .true.,&
          .true.,&
          .true.,&
          .true.,&
          .false.,&
          .false.,&
          .false.,&
          .true.,&
          .true.,&
          .true.,&
          .true.,&
          .false.,&
          .false.,&
          .true.,&
          .true./)

       !> Liste du type d'entité (élément=kElement ou noeud=kNoeud) support du champ
       integer, dimension(kNombreMaximumChamps), parameter :: kChampEntite = &
        (/kElement,&
          kElement,&
          kElement,&
          kElement,&
          kElement,&
          kNoeud,&
          kElement,&
          kElement,&
          kElement,&
          kElement,&
          kElement,&
          kElement,&
          kNoeud,&
          kElement,&
          kNoeud,&
          kElement,&
          kElement,&
          kElement,&
          kElement/)

       !> Liste du nombre de composantes du champ (3 pour vecteur, 1 pour scalaire)
       integer, dimension(kNombreMaximumChamps), parameter :: kChampNombreComposantes = &
        (/3,&
          3,&
          3,&
          3,&
          3,&
          3,&
          3,&
          1,&
          1,&
          1,&
          1,&
          3,&
          3,&
          1,&
          1,&
          3,&
          1,&
          3,&
          1/)

       !> Unite littérale du temps (SI=s)
       character(len=1), parameter :: kUniteTemps = 's'

       !> Constante NULL pour les indices de tableaux et pour les numeros d'inconnues.
       !! Le nom signifie (sans doute) entier (integer) ou logique non défini.
       INTEGER, PARAMETER :: kNil = 0
       !> Entier affecté à une inconnue (noeud ou arête) pour indiquer que cette inconnue est conditionnée,
       !! i.e., qu'elle ne doit pas être traitée comme une vraie inconnue mais, par exemple, qu'elle fait partie
       !! d'une condition de périodicité.
       !! Cet entier est choisi comme le plus grand entier possible : 2^31 = 2147483647.
       !! Il faut que le maillage contienne moins de 2147483647 (2 milliards) de noeuds ou d'éléments, ce qui est dans les limites actuelles.
       !! Un test est néanmoins réalisé dans le programme pour s'assurer de cette contrainte.
       INTEGER, PARAMETER :: kCdl = 2147483647


       !> Structure dédiée à la sauvegarde des solutions et au redémarrage de la simulation
       type typeSauvegardeRedemarrage
           !
           !> Type de comportement :
           !! - 'aucun' : simulation habituelle sans sauvegarde, par défaut,
           !! - 'sauvegarde' : simulation habituelle avec sauvegarde personnalisable par les autres paramètres,
           !! - 'redemarrage' : redémarrage de la simulation au pas de temps itempsRedemarrage,
           !!   ce qui nécessite la sauvegarde préalable de la solution au pas de temps précédent.
           !!   Les solutions aux pas de temps suivants sont sauvegardés aussi.
           !! - 'post-traitement' : post-traitement a posteriori à partir des solutions sauvegardées au préalable (désactivé).
           character(len=15) :: type
           !> Pas de temps de redémarrage de la solution
           integer :: itempsRedemarrage
           !> Continuer les sorties globales de post-traitement *.don (.true.) ou effacer ces fichiers lors du redémarrage (.false.)
           logical :: continuerPostProGlobal
           !> Sauvegarde de la solution (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           !! Ce qui est sauvegardé est :
           !! - la solution du système : systeme%X,
           !! - des grandeurs globales pour chaque inducteur (v1) : listeInducteurs(:)%courantPrecedent, %fluxMagnPrecedent, %tensionPrecedente,
           !! - des grandeurs globales pour chaque source magnétique (v1) : listeTermesSourcesMagn(:)%fluxMagnPrecedent, %ddpMagnPrecedent.
           !! Un numéro de version (kVersionSolution), enregistré en premier dans le fichier binaire, permet de prévoir l'évolution
           !!   future des données sauvegardées et de les relire correctement lors du redémarrage.
           !! La lecture reste compatible avec la version précédente (v0), qui ne contient que la solution du système
           !!  et relit quelques grandeurs globales (courant et flux magnétique dans les inducteurs) à partir des fichiers *.don.
           !! La comparaison à une solution de référence est possible aussi si .true. pour tout mode autre que la simulation habituelle.
           logical :: solution
           !> Sauvegarde du maillage (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           !! Par maillage on entend ce qui est lu par la routine lireMaillage, i.e. :
           !! - la liste des éléments (listeElements) : indice, milieu, type d'élément, noeuds associés,
           !! - la liste des noeuds (listeNoeuds) : coordonnées, correspondance entre noeuds et éléments, liste des groupes de noeuds,
           !! - le nombre d'éléments de chaque type (nombreElementsParType).
           logical :: maillage
           !> Sauvegarde des arêtes calculées par la routine calculerAretes (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           !! Ce qui est sauvegardé est :
           !! - nombreAretes : nombre d'arêtes, dimension du tableau listeAretes,
           !! - listeAretes(:)%indiceNoeudDepart
           !! - listeAretes(:)%indiceNoeudArrivee
           !! - listeElements(:)%listeIndicesAretes(1:nb) où nb est le nombre réel d'arêtes pour un élément donné
           logical :: aretes
           !> Sauvegarde des facettes calculées par la routine calculerFacettes (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           !! Ce qui est sauvegardé est :
           !! - nombreFacettes : nombre de facettes du maillage, dimension du tableau listeFacettes,
           !! - listeFacettes(:)%listeIndiceNoeuds : liste des noeuds définissant une facette donnée,
           !! - listeFacettes(:)%listeIndiceAretes : liste des arêtes définissant une facette donnée,
           !! - listeFacettes(:)%listeIndiceElements : liste des 1 ou 2 éléments connectés à une facette donnée,
           !! - listeElements(:)%listeIndicesFacettes(1:nb) où nb est le nombre réel de facettes pour un élément donné.
           logical :: facettes
           !> Sauvegarde des arêtes jaugées calculées par la routine calculerArbreAretes en cas de problème jaugé (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           !! Ce qui est sauvegardé est :
           !! - nombreAretes : nombre d'arêtes, dimension du tableau listeAretes,
           !! - listeAretes(:)%areteJauge
           logical :: jauge
           !> Sauvegarde des densités de courant J calculées par la routine calculerJ (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           !! Ce qui est sauvegardé est :
           !! - nombreFacettes : nombre de faces, dimension du tableau listeFacettes,
           !! - listeFacettes(:)%indiceInducteur(:),
           !! - listeFacettes(i)%fluxJNormalise(:),
           !! - listeInducteurs(:)%sectionEntree et listeInducteurs(:)%sectionSortie :
           !!   surfaces d'entrée et de sortie des inducteurs massifs, calculées par la routine calculerSurfaces,
           !! - listeTermesSourcesMagn(:)%sectionEntree et listeTermesSourcesMagn(:)%sectionSortie :
           !!   surfaces d'entrée et de sortie des termes sources magnétique, calculées par la routine calculerSurfaces.
           logical :: N
           !> Sauvegarde des circulations de K calculées par la routine calculerCirculationH (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           !! Ce qui est sauvegardé est :
           !! - nombreAretes : nombre d'arêtes, dimension du tableau listeAretes,
           !! - listeAretes(:)%circulationH,
           !! - listeAretes(:)%listeCirculationsHs(:),
           logical :: K
           !> Sauvegarde des points explorateurs du champ calculés par la routine rechercherPointsExplorateurs (.true.) ou non (.false.) en mode (type) de sauvegarde ou de redémarrage.
           logical :: pointsExplorateurs
           !> Nom générique du fichier contenant la sauvegarde des solutions.
           !! Ce nom est automatiquement complété par la formulation et l'indice de pas de temps,
           !! e.g., solution.sav devient solution_A_it3.sav.
           !! Ce paramètre permet de fédérer dans le code les fréquents appels à ce nom de fichier
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomSolution
           !> Nom générique du fichier contenant la sauvegarde du maillage
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomMaillage
           !> Indicateur de maillage chargé depuis un fichier de sauvegarde (.true.) ou non (.false.)
           logical :: maillageCharge
           !> Nom générique du fichier contenant la sauvegarde des arêtes
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomAretes
           !> Indicateur d'arêtes chargées depuis un fichier de sauvegarde (.true.) ou non (.false.)
           logical :: aretesChargees
           !> Nom générique du fichier contenant la sauvegarde des facettes
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomFacettes
           !> Indicateur de facettes chargées depuis un fichier de sauvegarde (.true.) ou non (.false.)
           logical :: facettesChargees
           !> Nom générique du fichier contenant la sauvegarde des arêtes jaugées
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomJauge
           !> Indicateur des arêtes jaugées chargées depuis un fichier de sauvegarde (.true.) ou non (.false.)
           logical :: jaugeChargee
           !> Nom générique du fichier contenant la sauvegarde des densités de courant J
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomN
           !> Indicateur de densite de courant J chargée depuis un fichier de sauvegarde (.true.) ou non (.false.)
           logical :: NCharge
           !> Nom générique du fichier contenant la sauvegarde des circulations de K
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomK
           !> Indicateur de circulations de K chargées depuis un fichier de sauvegarde (.true.) ou non (.false.)
           logical :: KCharge
           !> Nom générique du fichier contenant la sauvegarde des points explorateurs du champ
           !! Sa valeur est définie dans la routine initialiserCode ou configurationInitiale.f90.
           !! Ce n'est pas un paramètre utilisateur.
           character(len=50) :: nomPointsExplorateurs
           !> Indicateur de points explorateurs du champs chargés depuis un fichier de sauvegarde (.true.) ou non (.false.)
           logical :: pointsExplorateursCharge
       end type

       !> Configuration de la sauvegarde des solutions et du redémarrage de la simulation.
       !! Les paramètres de cette configuration sont structurés par le type typeSauvegardeRedemarrage.
       !! Ils sont accessibles depuis tout emplacement du code
       !
       type(typeSauvegardeRedemarrage) :: sav

       ! Constantes relatives au maillage MED
       !
       !> Affichage des groupes de noeuds et d'éléments, si TRUE.
       !! Ne fonctionne que pour les maillages MED.
       LOGICAL, PARAMETER :: kAfficheGroupes = .false.

       ! Constantes relatives au calcul par dichotomie d'une permeabilite
       ! magnetique non-lineaire a partir du champ magnetique H, et du
       ! calcul de l'energie magnetique dans un milieu non-lineaire.
       REAL(KIND(0D0)), PARAMETER  :: kBInfPermeabiliteMagnetiqueH = 0.0D0
       REAL(KIND(0D0)), PARAMETER  :: kBSupPermeabiliteMagnetiqueH = 2000.0D0
       REAL(KIND(0D0)), PARAMETER  :: kErreurDichotomieH           = 1.0D-05
       INTEGER, PARAMETER          :: kNombrePasIntegrale          = 100

       ! Constantes relatives a la factorisation incomplete de Crout.
       INTEGER, PARAMETER          :: kPivotsCrout        = 20
       REAL(KIND(0D0)), PARAMETER  :: kEpsilonPivotsCrout = 1.0D-7

        ! section liée à la gestion dynamique de la mémoire
        !> Nombre d'octets pour un entier long, en vue du calcul de la taille d'un tableau d'entiers.
        !! C'est aussi ce nom qui est utilisé pour déclarer un entier long : integer(kLong) ou une constante, e.g., 1_kLong.
        integer(8), parameter :: kLong = 8
        !> Nombre d'octets pour un entier, en vue du calcul de la taille d'un tableau d'entiers
        integer(kLong), parameter :: kInteger = 4
        !> Nombre d'octets pour un réel simple précision, en vue du calcul de la taille d'un tableau de tels réels
        integer(kLong), parameter :: kRealSimple = 4
        !> Nombre d'octets pour un réel double précision, en vue du calcul de la taille d'un tableau de tels réels.
        !! C'est le cas le plus utilisé dans le code, d'où son nom court
        integer(kLong), parameter :: kReal = 8
        !> Nombre d'octets pour une variable logique, en vue du calcul de la taille d'un tableau de tels logiques.
        !! Même taille qu'un entier par défaut, i.e., 4 octets.
        integer(kLong), parameter :: kLogical = 4
        !> Nombre d'octets pour un caractère, en vue du calcul de la taille d'un tableau de tels caractères.
        !! On utilise un octet par défaut, pour le codage ASCII utilisé dans le code
        integer(kLong), parameter :: kCharacter = 1
        !> Valeur de la mémoire maximale utilisée dans le code par des tableaux dynamiques, en octets,
        !! i.e., le "pic" mémoire, calculé
        integer(kLong) :: picMemoire
        !> Valeur de la mémoire statique utilisée dans le code, en octets,
        !! obtenue par la routine read_vmpeak() au lancement du code.
        integer(kLong) :: memoireStatiqueLue
        !> Valeur de la mémoire maximale utilisée dans le code par des tableaux dynamiques, en octets,
        !! i.e., le "pic" mémoire, obtenue par la routine read_vmpeak() au lancement du code.
        integer(kLong) :: picMemoireLu
        !> Nombre maximum de tableaux dynamiques (memoire) créés en mémoire à un instant donné, au démarrage du programme.
        !! Il peut y en avoir plus au total si certains tableaux sont détruits en cours de programme
        !! ou si certains tableaux ont même nom et lieu de création.
        !! Si le nombre de tableaux créés dépasse ce maximum, la taille de memoire est doublée automatiquement.
        integer, parameter :: kNombreInitialMemoire = 50
        !> Type contenant les informations pour un tableau dynamique
        type TYPE_MEMOIRE
        !> Nom du tableau créé
        character(len=80) :: nom
        !> Nom de la routine ou fonction ou ce tableau est créé
        character(len=80) :: lieuCreation
        !> Nombre de créations de ce tableau de même nom en un même lieu
        integer :: nombre
        !> Identifiant unique du tableau.
        !! Il est utilisé lorsque on ne peut pas savoir dans quel routine/fonction ou sous quel nom ce tableau est créé,
        !! e.g., plugins.
        character(len=200) :: identifiant
        !> Ce tableau est global, i.e., nom unique et disponible dans tout le programme (.true.) ou non (.false.)
        logical :: global
        !> Taille (en octets) de chaque tableau, dournie en option lors de la création.
        !! ATTENTION! Il peut y'avoir une ambiguité lorsque plusieurs tableaux de même nom sont créés au même endroit.
        !! La taille est alors la somme des tailles sur tous ces tableaux.
        integer(kLong) :: taille
        end type TYPE_MEMOIRE

        !> Calcul de la taille du type TYPE_MEMOIRE en octets
        integer(kLong), parameter :: kTailleTYPE_MEMOIRE = (80+80+200)*kCharacter + kInteger + kLogical + kLong

        !> liste des tableaux en mémoire dynamique
        type(TYPE_MEMOIRE), dimension(:), allocatable :: memoire

       ! Réglages relatifs au maillage
       !> Unités du maillage, sous forme d'une échelle (réel) multiplicateur des coordonnées,
       !! afin d'obtenir un maillage S.I. (en mètres).
       !! Les valeurs sont par exemple 1 (mètres) ou 1e-3 (millimètres).
       !! ATTENTION! Ceci n'affecte que les maillages au format MED avec librairie version 3.
       REAL(KIND(0D0)) :: echelleMaillage
       !> Format du maillage (UNV, MED)
       integer :: formatMaillage

       ! Constantes relatives au gradient conjugue et a la resolution.
       ! Nombre d iterations max.
       !> Précision linéaire demandée (comparable à la précision machine)
       REAL(KIND(0D0)) :: kEpsilonGCP
       !> Type de solveur lineaire (par ordre croissant de cout memoire mais avec une meilleure robustesse)
       !!
       !! - 0 : GC sans preconditionneur,
       !! - 1 : GCPC avec preconditionneur Crout,
       !! - 2 : GCPC avec preconditionneur Jacobi,
       !! - 3 : GCPC avec preconditionneur MUMPS (MUMPS doit etre installe sinon erreur),
       !! - 4 : Solveur direct MUMPS             (MUMPS doit etre installe sinon erreur),
       !! - 5 : Estimation des besoins memoires.
       INTEGER         :: TypeSolveurLineaire
       !> Nombre maximal d'itérations du solveur linéaire
       INTEGER         :: nbIterationMax
       !> Activation du monitoring de l'etape de resolution des systemes lineaires
       INTEGER         :: Imonitoring_systeme
       !> Activation du monitoring de l'etape de resolution des pbs non lineaires
       INTEGER         :: Imonitoring_methodeNL
       !> Frequence reactualisation du solveur lineaire (preconditionneur...)
       INTEGER         :: reacprecond_methodeNL
       !> Timers pour monitoring global de l'etape systeme lineaire
       !! remis a zero pour chaque pas de temps.
       REAL(KIND(0D0)) :: total_time_cpu_sl,total_time_elapsed_sl
       !> Nbre total d'iterations du GCPC par pas de temps
       INTEGER         :: total_nbiter_GCPC
       !> Utilisation du solveur GC legacy (version 1.11.1 et antérieure)
       logical :: utiliserGClegacy

       !
       ! Parametres condenses pour l'utilisation de MUMPS...
       !
       !> Choix du renumeroteur MUMPS
       ! Les choix possibles sont : "AUTO", "AMD", "AMF", "QAMD", "PORD", "METIS", "SCOTCH", "PARMETIS", "PTSCOTCH"
       CHARACTER(LEN=8):: mumps_renum
       !> Gestion des objets MUMPS en memoire
       CHARACTER(LEN=8):: mumps_memory
       !> Valeur entiere (en %) de l'espace supplementaire pour le pivotage
       INTEGER         :: mumps_pivot
       !> Activation des pretraitements
       CHARACTER(LEN=8):: mumps_pre
       !> Activation des post-traitements
       CHARACTER(LEN=8):: mumps_post
       !> Valeur reelle maximale admise sur la borne d'erreur (forward error)
       REAL(KIND(0D0)) :: kEpsilonMUMPS
       !> Activation d'eventuelles procedures d'auto-correction en cas de pb
       LOGICAL         :: Lmumps_autocorrec
       !> Parametre reel pour relaxer les valeurs matricielles fournies a MUMPS
       REAL(KIND(0D0)) :: mumps_relax


       !> Type element de reference.
       !! Cf. ci-dessous pour la description de ses propriétés.
       TYPE ELEMENT_REFERENCE
         !> Nombre de noeuds
         INTEGER :: nombreNoeuds
         !> Nombre d'arêtes
         INTEGER :: nombreAretes
         !> Nombre de facettes
         INTEGER :: nombreFacettes
         !> Liste d'arêtes orientées selon la numérotation locale des noeuds
         INTEGER, DIMENSION(1:kNombreMaximumAretesElement) :: listeAretes
         !> Liste d'arêtes orientées selon la numérotation locale des noeuds
         INTEGER, DIMENSION(1:kNombreMaximumFacettesElement) :: listeFacettes
         !> Liste de signes de normale aux facettes. Ces sens sont donnes
         !! forme reelle car ils sont employes dans des produits vectoriels de
         !! vecteurs reels.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumFacettesElement) :: signesNormales
         !> liste d'arêtes appartenant aux facettes
         INTEGER, DIMENSION(1:kNombreMaximumAretesFacettes) :: listeAretesFacettes
         !> Coordonnées cartésiennes (x,y,z) du centre (barycentre) de l'élément (coordonnées de référence)
         REAL(KIND(0D0)), DIMENSION(1:3) :: centre
       END TYPE

        !> Calcul de la taille du type ELEMENT_REFERENCE en octets
        integer(kLong), parameter :: kTailleELEMENT_REFERENCE = (kNombreMaximumFacettesElement+3)*kReal &
         + (3+kNombreMaximumAretesElement+kNombreMaximumFacettesElement+kNombreMaximumAretesFacettes)*kInteger

       ! Element de reference Triangle.
       ! ATTENTION! définition non testée, ne fonctionne pas en MED
       TYPE(ELEMENT_REFERENCE), PARAMETER :: kTriangle = &
         ELEMENT_REFERENCE(kNombreNoeudsTriangle, &
                           kNombreAretesTriangle, &
                           kNombreFacettesTriangle, &
                           (/12, 13, 23, kNil, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil/), &
                           (/123, kNil, kNil, kNil, kNil, kNil/), &
                           (/1.0D0, 0.0D0, 0.0D0, 0.0D0, 0.0D0, 0.0D0/), &
                           (/1, 2, 3, kNil, kNil, kNil, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil, kNil, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil, kNil/),&
                           (/kNil, kNil, kNil/) ) ! coordonnées du centre de l'élément non utilisées

       ! Element de reference Quadrangle.
       ! ATTENTION! définition non testée
       TYPE(ELEMENT_REFERENCE), PARAMETER :: kQuadrangle = &
         ELEMENT_REFERENCE(kNombreNoeudsQuadrangle, &
                           kNombreAretesQuadrangle, &
                           kNombreFacettesQuadrangle, &
                           (/12, 23, 34, 14, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil/), &
                           (/1234, kNil, kNil, kNil, kNil, kNil/), &
                           (/1.0D0, 0.0D0, 0.0D0, 0.0D0, 0.0D0, 0.0D0/), &
                           (/1, 2, 3, 4, kNil, kNil, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil, kNil, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil, kNil/),&
                           (/kNil, kNil, kNil/) ) ! coordonnées du centre de l'élément non utilisées

       ! Element de reference Tetraedre.
       TYPE(ELEMENT_REFERENCE), PARAMETER :: kTetraedre = &
         ELEMENT_REFERENCE(kNombreNoeudsTetraedre, &
                           kNombreAretesTetraedre, &
                           kNombreFacettesTetraedre, &
                           (/12, 13, 14, 23, 24, 34, kNil, kNil, kNil, kNil, &
                             kNil, kNil/), &
                           (/123, 124, 134, 234, kNil, kNil/), &
                           (/1.0D0, - 1.0D0, 1.0D0, - 1.0D0, 0.0D0, 0.0D0/), &
                           (/1, 2, 4, kNil, 1, 3, 5, kNil, 2, 3, 6, kNil, &
                             4, 5, 6, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil, kNil/),&
                           (/0.25D0, 0.25D0, 0.25D0/) ) ! coordonnées du centre de l'élément

       ! Element de reference Pyramide.
       ! ATTENTION! définition non testée et pas bien réglée
       TYPE(ELEMENT_REFERENCE), PARAMETER :: kPyramide = &
         ELEMENT_REFERENCE(kNombreNoeudsPyramide, &
                           kNombreAretesPyramide, &
                           kNombreFacettesPyramide, &
                           (/12, 13, 14, 23, 24, 34, kNil, kNil, kNil, kNil, &
                             kNil, kNil/), &
                           (/123, 124, 134, 1234, kNil, kNil/), &
                           (/1.0D0, - 1.0D0, 1.0D0, - 1.0D0, 0.0D0, 0.0D0/), &
                           (/1, 2, 4, kNil, 1, 3, 5, kNil, 2, 3, 6, kNil, &
                             4, 5, 6, kNil, kNil, kNil, kNil, kNil, kNil, &
                             kNil, kNil, kNil/),&
                           (/0.4D0, 0.4D0, 0.2D0/) ) ! coordonnées du centre de l'élément

       ! Element de reference Prisme.
       TYPE(ELEMENT_REFERENCE), PARAMETER :: kPrisme = &
         ELEMENT_REFERENCE(kNombreNoeudsPrisme, &
                           kNombreAretesPrisme, &
                           kNombreFacettesPrisme, &
                           (/12, 13, 14, 23, 25, 36, 45, 46, 56, kNil, kNil, &
                             kNil/), &
                           (/123, 456, 1245, 1346, 2356, kNil/), &
                           (/- 1.0D0, 1.0D0, 1.0D0, - 1.0D0, 1.0D0, 0.0D0/), &
                           (/1, 2, 4, kNil, 7, 8, 9, kNil, 1, 3, 5, 7, &
                             2, 3, 6, 8, 4, 5, 6, 9, kNil, kNil, kNil, kNil/),&
                           (/0.33333333333333333D0, 0.33333333333333333D0, 0.0D0/) ) ! coordonnées du centre de l'élément

       ! Element de reference Hexaedre.
       TYPE(ELEMENT_REFERENCE), PARAMETER :: kHexaedre = &
         ELEMENT_REFERENCE(kNombreNoeudsHexaedre, &
                           kNombreAretesHexaedre, &
                           kNombreFacettesHexaedre, &
                           (/12, 13, 15, 24, 26, 34, 37, 48, 56, 57, 68, 78/), &
                           (/1234, 1256, 1357, 2468, 3478, 5678/), &
                           (/1.0D0, - 1.0D0, 1.0D0, - 1.0D0, 1.0D0, - 1.0D0/), &
                           (/1, 2, 4, 6, 1, 3, 5, 9, 2, 3, 7, 10, &
                             4, 5, 8, 11, 6, 7, 8, 12, 9, 10, 11, 12/),&
                           (/0.0D0, 0.0D0, 0.D0/) ) ! coordonnées du centre de l'élément

       ! Liste des elements de reference.
       TYPE(ELEMENT_REFERENCE), DIMENSION(1:kNombreElementsReference), &
         PARAMETER :: listeElementsReference = &
                        (/kTetraedre, kPrisme, kPyramide, kHexaedre/)
!         PARAMETER :: listeElementsReference = &
!                        (/kTetraedre, kPrisme, kPyramide, kHexaedre, kTriangle, kQuadrangle/)

       ! Liste des signes pour les circulations sur les aretes des facettes.
       REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesFacette), PARAMETER :: &
         kSignesCirculations = (/1.0D0, - 1.0D0, 1.0D0, - 1.0D0/)

       !> Type noeud du maillage. Un noeud comprend:
       !! - 3 coordonnees cartesiennes.
       !! - le nombre d'elements auxquels le noeud appartient.
       !! - la liste des indices d'elements auxquels le noeud appartient.
       !! - la liste booleene des groupes d'appartenance.
       !! - un numero d'inconnue.
       TYPE NOEUD
         REAL(KIND(0D0))  :: x
         REAL(KIND(0D0))  :: y
         REAL(KIND(0D0))  :: z
         INTEGER :: degreNoeudElements
         INTEGER, DIMENSION(:), ALLOCATABLE :: listeIndicesElements
         LOGICAL, DIMENSION(1:kNombreMaximumGroupes) :: listeGroupes
         LOGICAL, DIMENSION(1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn) :: presenceAlpha
         INTEGER :: numeroInconnue
         INTEGER, DIMENSION(1:knombreMaximumconditionPeridicite) :: noeudCorrespondant
       END TYPE
       !> Taille en octets du type NOEUD
       !integer(kLong), parameter :: kTailleNOEUD = 3*kReal + 2*kInteger + kDegreMaximumNoeudElements*kInteger &
       !                         + (kNombreMaximumGroupes+kNombreMaximumInducteurs+kNombreMaximumSourceMagn)*kLogical &
       !                         + knombreMaximumconditionPeridicite*kInteger
       integer(kLong), parameter :: kTailleNOEUD = 3*kReal + 2*kInteger &
                                + (kNombreMaximumGroupes+kNombreMaximumInducteurs+kNombreMaximumSourceMagn)*kLogical &
                                + knombreMaximumconditionPeridicite*kInteger

       !> Type arete du maillage. Une arete comprend:
       !! - un indice de noeud de depart.
       !! - un indice de noeud d'arrivee.
       !! - une valeur de circulation pour le champ magnetique.
       !! - un numero d'inconnue.
       !! - une liste de circulations pour le champ source par inducteur.
       TYPE ARETE
         INTEGER :: indiceNoeudDepart
         INTEGER :: indiceNoeudArrivee
         REAL(KIND(0D0))  :: circulationH
         INTEGER :: numeroInconnue
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn) ::&
                                                                         listeCirculationsHs
         LOGICAL :: areteJauge
         logical :: areteinternemilieu
         INTEGER, DIMENSION(1:knombreMaximumconditionPeridicite) :: areteCorrespondant
       END TYPE
       !> Taille en octets du type ARETE
       integer(kLong), parameter :: kTailleARETE = 3*kInteger + kReal &
                                + (kNombreMaximumInducteurs+kNombreMaximumSourceMagn)*kReal &
                                + kLogical + knombreMaximumconditionPeridicite*kInteger

       !> Type facette du maillage. Une facette comprend:
       !! - une liste d'indices de noeuds.
       !! - une liste d'indices d'aretes.
       !! - une liste d'indices d'elements.
       !! - un indice d'inducteur ("kNil" si pas d'inducteur).
       !! - flux de J normalise par terme source.
       TYPE FACETTE
         INTEGER, DIMENSION(1:kNombreMaximumNoeudsFacette) :: listeIndicesNoeuds
         INTEGER, DIMENSION(1:kNombreMaximumAretesFacette) :: listeIndicesAretes
         INTEGER, DIMENSION(1:2) :: listeIndicesElements
         LOGICAL, DIMENSION(1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn) :: indiceInducteur
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn) ::fluxJNormalise
         REAL(KIND(0D0))  :: SigneNormale
       END TYPE
       !> Taille en octets du type FACETTE
       integer(kLong), parameter :: kTailleFACETTE = 2*kNombreMaximumNoeudsFacette*kInteger + 2 &
                                + (kNombreMaximumInducteurs+kNombreMaximumSourceMagn)*(kLogical+kReal) + kReal

       !> Type element du maillage. Un element comprend:
       !! - un indice de milieu IDEAS.
       !! - l'indice de l'element de reference.
       !! - l'indice dans le maillage, égal à la liste en UNV I-Deas mais différent a priori en MED
       !! - un indice d'inducteur.
       !! - une liste d'indices de noeuds.
       !! - une liste d'indices (positifs ou negatifs) d'aretes.
       !! - une liste d'indices de facettes.
       TYPE ELEMENT
         LOGICAL, DIMENSION(1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn) :: presenceK
         LOGICAL, DIMENSION(1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn) :: presenceAlpha
         INTEGER :: indiceMilieu
         INTEGER :: indiceElementReference
         INTEGER :: indiceMaillage
         INTEGER :: indiceInducteur
         ! JPD UNV Salome et sortie VTK
         INTEGER :: indiceElement
         INTEGER :: fe_descriptor_id
         INTEGER :: physicalProperty
         INTEGER :: materialProperty
         INTEGER :: couleur
         !/JPD
         INTEGER :: nombreNoeudsElement
         integer, dimension(1:kNombreMaximumArbreCirc) :: listeIndiceMaille
         INTEGER, DIMENSION(1:kNombreMaximumNoeudsElement) :: listeIndicesNoeuds
         INTEGER, DIMENSION(1:kNombreMaximumAretesElement) :: listeIndicesAretes
         INTEGER, DIMENSION(1:kNombreMaximumFacettesElement) :: &
                                                            listeIndicesFacettes
         LOGICAL :: elementEnMouvement
       END TYPE
       !> Taille en octets du type ELEMENT
       integer(kLong), parameter :: kTailleELEMENT = 2*(kNombreMaximumInducteurs+kNombreMaximumSourceMagn)*kLogical &
                                + 4*kInteger &
            + (kNombreMaximumNoeudsElement+kNombreMaximumAretesElement+kNombreMaximumFacettesElement)*kInteger &
                                + kLogical

       !> Type point 3D. Un point 3D comprend:
       !! - 3 coordonnees cartesiennes.
       TYPE POINT
         REAL(KIND(0D0)) :: x
         REAL(KIND(0D0)) :: y
         REAL(KIND(0D0)) :: z
       END TYPE

       !> Taille d'un type POINT
       integer(kLong), parameter :: kTaillePOINT = 3*kReal

      !> Contient les interpolés sur les elements, ainsi que diverses informations sur le point de l'élément en lequel l'interpolation a lieu.
      TYPE INTERPOLATION
      !> Coordonnées du point de Gauss ou précisé par l'utilisateur, en lequel l'interpolation a lieu (coordonnées de référence)
      TYPE(POINT) :: pointCourant
      !> Pondération de ce point si point de Gauss
      REAL(KIND(0D0)) :: ponderation
      !> Valeurs des fonctions d'aretes en un point de Gauss dans l'element reel.
      REAL(KIND(0D0)),DIMENSION(1:3, 1:kNombreMaximumAretesElement) :: valeursAretes
      !> Valeurs des rotationnels en un point de Gauss dans l'element reel.
      REAL(KIND(0D0)),DIMENSION(1:3, 1:kNombreMaximumAretesElement) :: rotationnels
      !> Matrice transposees des fonctions d'aretes dans l'element reel.
      REAL(KIND(0D0)),DIMENSION(1:kNombreMaximumAretesElement, 1:3) :: tValeursAretes
      !> Matrice transposee des rotationneldans l'element de reference.
      REAL(KIND(0D0)),DIMENSION(1:kNombreMaximumAretesElement, 1:3) :: tRotationnels
      !> Valeurs des gradients en un point de Gauss dans l'element reel.
      REAL(KIND(0D0)),DIMENSION(1:3, 1:kNombreMaximumNoeudsElement) :: gradients
      !> Transposee de la matrice gradients.
      REAL(KIND(0D0)),DIMENSION(1:kNombreMaximumNoeudsElement, 1:3) :: tGradients
      !> Transposee de la matrice gradients de référence
      REAL(KIND(0D0)), DIMENSION(1:3, 1:kNombreMaximumNoeudsElement) :: tGradientsRef
      !> Matrice jacobienne
      REAL(KIND(0D0)), DIMENSION(1:3, 1:3) :: jacobien
      !> Inverse de la matrice Jacobienne
      REAL(KIND(0D0)), DIMENSION(1:3, 1:3) :: iJacobien
      !> Déterminant de la transformation de l'élément de référence en élément réel
      REAL(KIND(0D0)) :: det
      !> Valeurs des fonctions nodales en un point de Gauss dans l'élément de référence
      REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement) :: valeursNodales
      !> Valeurs des fonctions de facettes en un point de Gauss dans l'element reel.
      REAL(KIND(0D0)),DIMENSION(1:3, 1:kNombreMaximumFacettesElement)::  valeursFacettes
      !> alpha est utilisé avec les termes source magnétiques et/ou les inducteurs massifs
      REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumSourceMagn+kNombreMaximumInducteurs,1:kNombreMaximumNoeudsElement) :: alpha
      !> Valeur du champ B
      REAL(KIND(0D0)),DIMENSION(1:3):: champB
      !> Valeur du champ H
      REAL(KIND(0D0)),DIMENSION(1:3):: champH
      !> Valeur du champ J
      REAL(KIND(0D0)),DIMENSION(1:3):: champJ
      !> Valeur du champ E
      REAL(KIND(0D0)),DIMENSION(1:3):: champE
      REAL(KIND(0D0)),DIMENSION(1:3):: champA
      !> Valeur du potentiel Omega
      REAL(KIND(0D0)) :: champOmega
      !> Matrice MNF (abréviation de matrice des fonctions nodales factorisées) qui permet,
      !!  pour un point donné dans un élément, de passer des coordonnées des coordonnées dans l'élément de référence aux coordonnées réelles,
      !!  selon la définition x = x0 + MNF*xi,
      !!  où x et xi sont les coordonnées (cartésiennes 3D) du point dans l'espace réel et dans l'élément de référence, respectivement,
      !!  et x0 est la translation de l'élément réel par rapport au centre de l'élément de référence.
      REAL(KIND(0D0)), DIMENSION(1:3, 1:3) :: MNF
      !> Matrice dMNF, basée sur la matrice MNF et sa dérivée par rapport aux coordonnées dans l'élément de référence.
      !! Elle est utilisée par la méthode de Newton-Raphson lors de la recherche non-linéaire d'un point quelconque dans l'élément de référence.
      !! Le problème à résoudre est mis sous la forme f(xi) = MNF(xi)*xi - (x-x0) et on calcule f'(xi) = xi*d/dxi[MNF(xi)] + MNF(xi).
      !! Sa définition est alors : dMNF = xi.dMNF/dxi + MNF (vectoriel) = MNF + xi*dMNF/dxi + eta*dMNF/deta + zeta*dMNF/dzeta (développé en composantes).
      REAL(KIND(0D0)), DIMENSION(1:3, 1:3) :: dMNF
      !> Vecteur de translation x0 de l'élément réel par rapport au centre de l'élément de référence (cf. définition dans iMNF ci-dessus)
      REAL(KIND(0D0)), DIMENSION(1:3) :: translationRefReel
      END TYPE

       ! Type points de Gauss. Une liste de points de Gauss comprend:
       ! - l'indice de l'element de reference.
       ! - la facette de reference (interpolation 2D, "kNil" si interpolation
       !   3D).
       ! - le nombre de points de la liste.
       ! - la liste des points.
       ! - la liste des ponderations.
       TYPE POINTS_GAUSS
         INTEGER :: indiceElementReference
         INTEGER :: codefacetteReference
         INTEGER :: nombrePoints
         TYPE(POINT), DIMENSION(1:knombreMaximumPointsGauss) :: listePoints
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumPointsGauss) :: &
                                                               listePonderations
       END TYPE

       !> Taille d'un type POINTS_GAUSS
       integer(kLong), parameter :: kTaillePOINTS_GAUSS = 3*kInteger &
            + knombreMaximumPointsGauss*kTaillePOINT + kNombreMaximumPointsGauss*kReal

    !> Propriétés des pertes (Fer conducteur ou non, cuivre) dans un milieu.
    type typePertes
        !> Toutes les données du type sont validées (.true.) ou non (.false.)
        logical  :: set
        !> Modèle utilisé (cf. paramètres kPertes... plus loin)
        integer  :: model
        !> frequence utilisée pour certains modèles de composantes (Hz)
        real(kind(0d0)) :: frequency
        !> Coefficient (volumique ou massique, cf. units) de la composante quasi-statique (hysteris)
        real(kind(0d0)) :: kh
        !> Exposant de la composante hysteresis, sans unité
        real(kind(0d0)) :: alpha
        !> Coefficient (volumique ou massique, cf. units) de la première composante dynamique (courants induits, aka eddy currents)
        real(kind(0d0)) :: kec
        !> Coefficient (volumique ou massique, cf. units) de la deuxième composante dynamique (excédentaire, aka excess loss)
        real(kind(0d0)) :: kexc
        !> Axe normal (OX, OY, OZ) au plan de rotation du champ B dans le milieu.
        !! Il prend les valeurs kAxeOX, kAxeOY, kAxeOZ, respectivement, ou kAxeAuto pour un choix automatique de cet axe, élément par élément.
        integer  :: components_normal_axis
        !> Prise en compte du champ B sur l'axe majeur, mineur ou les 2 axes de l'ellipse formée dans le plan de rotation.
        !! Il prend les valeurs kEllipse2Axes, kEllipseAxeMajeur, et kEllipseAxeMineur.
        integer  :: components
        !> Unité des coefficients des composantes : volumique (kPertesFerVolumique) ou massique (kPertesFerMassique).
        !! Ceci n'est pas nécessaire pour du cuivre ou du fer conducteur, car l'unité est volumique forcément, car on utilise la densité volumique des pertes Joule instantanées.
        integer  :: units
        !> Nombre de répétitions du signal, lors de l'analyse FFT, pour augmenter les fréquences.
        integer  :: fft_nrepeat
        !> Modèle utilisé pour la composante hysteresis, e.g., kDB2.
        integer :: model_h
        !> Modèle utilisé pour la composante courants induits , e.g., kINT.
        integer :: model_ec
        !> Modèle utilisé pour la composante excédentaire, e.g., kINT.
        integer :: model_exc
        !> Modèle utilisé pour les pertes Joule, e.g., kINT.
        integer :: model_pj
        !> Au moins une composante utilise le modèle kBMAX (usage interne)
        logical :: use_bmax
        !> Au moins une composante utilise le modèle kDB2 (usage interne)
        logical :: use_db2
        !> Au moins une composante utilise le modèle kINT (usage interne)
        logical :: use_int
        !> Au moins une composante utilise l'analyse FFT (usage interne)
        logical :: utiliserFFT
        !> Nom du matériau (usage interne)
        character(len=80) :: name
        !> Utilisation du champ B (usage interne)
        logical :: utiliserChampB
        !> Utilisation du champ des Pertes Joule volumiques (usage interne)
        logical :: utiliserChampPJ
        !> Utilisation du champ Jinduit pour analyse FFT uniquement (usage interne)
        logical :: utiliserChampJ
        !
    end type typePertes

    !> Taille d'un type typePertes
    integer(kLong), parameter :: kTailletypePertes = 8*kLogical + 9*kInteger + 5*kReal + 80*kCharacter

    !> Configuration générale du post-traitement des pertes,
    !! i.e., pour tous les milieux, en lien avec la configuration par milieu typePertes.
    type typePertesPost
        !> Toutes les données du type sont validées (.true.) ou non (.false.)
        logical  :: set
        !> Période de calcul des pertes Fer (s)
        real(kind(0d0)) :: periode
        !> Valeur du temps de fin de calcul des pertes Fer, le calcul se faisant sur le période précédent ce temps (s).
        !! Il est en principe équivalent à l'indice du temps de fin.
        !! Si l'indice de ce temps est positif, c'est ce dernier qui est utilisé et ce temps de fin est calculé.
        real(kind(0d0)) :: temps_fin
        !> Pas de temps lorsque constant (s)
        !! C'est temporaire.
        real(kind(0d0)) :: dt
        !> Indice du temps de fin de calcul des pertes Fer, le calcul se faisant sur la période précédent ce temps.
        !! Il est en principe équivalent au temps de fin, mais il est utilisé en priorité si positif.
        integer :: indice_fin
        !> Liste des éléments (label maillage) pour lesquels le loci du champ B est écrit dans un fichier,
        !! i.e., les valeurs du champ B pour tous les pas de temps de la période sélectionnée.
        integer, dimension(:), allocatable :: loci_elements
        !> Liste des éléments (label maillage) pour lesquels l'analyse FFT sur le champ B et/ou sur le champ des pertes Joule est écrit dans un fichier,
        !! i.e., les valeurs (amplitude, phase) des harmoniques du champ pour tous les pas de temps de la période sélectionnée.
        integer, dimension(:), allocatable :: fft_elements
        !> Calcul de l'évolution des pertes Fer (.true.) ou non (.false.).
        !! C'est un  paramètre à usage interne, pour distinguer la sortie CSV ou .data.
        logical :: evolution
        !> Au moins un milieu utilise l'analyse FFT (usage interne)
        logical :: utiliserFFT
        !
    end type typePertesPost

    !> Taille d'un type typePertesPost
    integer(kLong), parameter :: kTailletypePertesPost = 3*kLogical + kInteger + 3*kReal

    !> Constante pour les pertes excédentaires.
    !! Cette constante correspond au passage entre l'expression intégrale et l'expression Bmax pour un signal sinusoïdal,
    !!  i.e., 1/T \int_0^T |dB/dt|^1.5 dt = kexc_cte * (f*Bm)^1.5, où B(t) = Bm*sin(2\pi*t/T) et T est la période du signal.
    !! On trouve alors kexc_cte =  \sqrt(2*\pi) * \int_0^(2*\pi) |sin(\theta)|^1.5 d \theta.
    !! Le calcul ci-dessous est réalisé avec le logiciel libre Maxima.
    !! Cette constante est utilisée de façon à garder la même valeur pour le coefficient kexc, issu de la caractérisation,
    !! quel que soit le modèle utilisé.
    real(kind(0d0)), parameter :: kexc_cte = 8.763364804397909d0
    !> Bertotti model with DB/2 for every 3 components (quasi-static hysteresis, eddy currents, excess loss), i.e., Mircea Fratila's M1 model.
    integer, parameter :: kPertesFerModeleBertotti_DB2_DB2_DB2 = 1
    !> Bertotti model with DB/2 for quasi-static hysteresis and time-integration for the two last components, i.e., Mircea Fratila's M2 model.
    integer, parameter :: kPertesFerModeleBertotti_DB2_INT_INT = 2
    !> Bertotti model with Fast Fourier Transform for quasi-static hysteresis and time-integration for the two last components, i.e., Mircea Fratila's M3 model.
    integer, parameter :: kPertesFerModeleBertotti_FFT_INT_INT = 3
    !> Bertotti model with automatic computation of frequency (FFT major frequency) for quasi-static hysteresis and time-integration for the two last components, i.e., Mircea Fratila's M3 model.
    integer, parameter :: kPertesFerModeleBertotti_AUTOFREQ_INT_INT = 4
    !> Bertotti model where model for each component may be chosen by hand
    integer, parameter :: kPertesFerModeleBertotti = 5
    !> Pertes dans le cuivre, i.e., matériau conducteur non-hystérétique, avec intégration temporelle sur les pertes Joule
    integer, parameter :: kPertesCuivre_INT = 6
    !> Pertes dans le cuivre, i.e., matériau conducteur non-hystérétique, avec choix de la méthode de calcul des pertes en fonction des pertes Joule
    integer, parameter :: kPertesCuivre = 7
    !> Pertes dans le Fer conducteur, i.e., Modèle de Bertotti pour la composante statique (DB2) + intégration temporelle sur les pertes Joule
    integer, parameter :: kPertesFerConducteurModeleBertotti_DB2_INT = 8
    !> Pertes dans le Fer conducteur, i.e., Modèle de Bertotti pour la composante statique + pertes Joule où les 2 modèles sont à choisir
    integer, parameter :: kPertesFerConducteurModeleBertotti = 9
    !> character strings in the block corresponding to these models
    character(len=42), dimension(9), parameter :: kPertesFerNomsModeleBertotti = (/&
        'kPertesFerModeleBertotti_DB2_DB2_DB2      ',&
        'kPertesFerModeleBertotti_DB2_INT_INT      ',&
        'kPertesFerModeleBertotti_FFT_INT_INT      ',&
        'kPertesFerModeleBertotti_AUTOFREQ_INT_INT ',&
        'kPertesFerModeleBertotti                  ',&
        'kPertesCuivre_INT                         ',&
        'kPertesCuivre                             ',&
        'kPertesFerConducteurModeleBertotti_DB2_INT',&
        'kPertesFerConducteurModeleBertotti        '/)
    !> Indice pour la composante Hysteresis
    integer, parameter :: kPertesHysteresis = 1
    !> Indice pour la composante Courants Induits
    integer, parameter :: kPertesCourantsInduits = 2
    !> Indice pour la composante des pertes excédentaires
    integer, parameter :: kPertesExcedentaires = 3
    !> Indice pour la composante des pertes Joules
    integer, parameter :: kPertesJoules = 4
    !> character strings corresponding to Bertotti components names, printed in the .data file.
    !! Each name has no blanck spaces, as defined in .data specification
    !! Ce tableau est en relation avec les indices définis précédemment, e.g. kPertesHysteresis.
    character(len=15), dimension(4), parameter :: kPertesFerModeleBertottiNomsComposantes = (/&
        'Hysteresis     ',&
        'CourantsInduits',&
        'Excedentaires  ',&
        'PertesJoule    '/)

    !
    ! Internal use in order to separate computations on each component
    !
    !> DB/2 model for the component
    integer, parameter :: kDB2 = 1
    !> time-integration model for the component
    integer, parameter :: kINT = 2
    !> Fast Fourier transform model for the component
    integer, parameter :: kFFT = 3
    !> Maximum value of B (amplitude) model for the component
    integer, parameter :: kBMAX = 4
    !> Automatic computation of frequency (FFT major frequency)
    integer, parameter :: kAUTOFREQ = 5
    !> character strings in the block corresponding to these components
    character(len=9), dimension(5), parameter :: kPertesFerModeleBertottiNomsComposante = (/&
        'kDB2     ',&
        'kINT     ',&
        'kFFT     ',&
        'kBMAX    ',&
        'kAUTOFREQ'/)

    !> Axe OX, e.g., pour la rotation, ou direction X selon le contexte
    integer, parameter :: kAxeOX = 1
    !> Axe OY, e.g., pour la rotation, ou direction Y selon le contexte
    integer, parameter :: kAxeOY = 2
    !> Axe OZ, e.g., pour la rotation (par défaut), ou direction Z selon le contexte
    integer, parameter :: kAxeOZ = 3
    !> Choix automatique de la direction (X, Y ou Z) normale au plan de rotation du champ B, lors du calcul des pertes
    integer, parameter :: kAxeAuto = 4
    !> character strings in the block corresponding to these components normal axis
    character(len=8), dimension(4), parameter :: kNomAxes = (/&
        'kAxeOX  ',&
        'kAxeOY  ',&
        'kAxeOZ  ',&
        'kAxeAuto'/)

    !> Components computation of both major and minor axis
    integer, parameter :: kEllipse2Axes = 1
    !> Components computation of major axis only
    integer, parameter :: kEllipseAxeMajeur = 2
    !> Components computation of minor axis only
    integer, parameter :: kEllipseAxeMineur = 3
    !> character strings in the block corresponding to these components
    character(len=17), dimension(3), parameter :: kNomAxesEllipse = (/&
        'kEllipse2Axes    ',&
        'kEllipseAxeMajeur',&
        'kEllipseAxeMineur'/)

    !> Volumic units for kh, kec, and kexc coefficients
    integer, parameter :: kPertesFerVolumique = 1
    !> Massic units for kh, kec, and kexc coefficients
    integer, parameter :: kPertesFerMassique = 2
    !> character strings in the block corresponding to these units
    character(len=19), dimension(2), parameter :: kPertesFerNomsUnites = (/&
        'kPertesFerVolumique',&
        'kPertesFerMassique '/)


       !> Type milieu. Un milieu comprend :
       !! - une valeur de conductivite (istrope ou non)
       !! - une valeur de permeabilite lineaire.
       !! - une permeabilite non-lineaire (coefficients de Marrocco).
       !! - un flag indiquant si les coordonnees polaires sont utilisees.
       !! - le point origine du repere polaire si utilisation.
       !! - l'axe du vecteur. Cet axe correspond au vecteur dans le cas des
       !!   coordonnees cartesiennes, et a son axe de rotation dans le cas
       !!   des coordonnees polaires.
       !! - Section de l'inducteur.
       !! - nombre de spires.
       !! - indique si le milieu est un groupe d elements.
       !! - si groupe element alors on a un flux.
       !! - une masse volumique (SI=kg/m3), 0 par défaut si non définie.
       !! - éventuellement des pertes Fer (hystérésis+...) ou cuivre (courants induits).
       TYPE MILIEU
         !> Conductivité isotrope
         REAL(KIND(0D0))                 :: conductivite
         !> Conductivité anisotrope vectorielle (x,y,z)
         REAL(KIND(0D0)), DIMENSION(1:3) :: conductivitev
         !> Conductivité anisotrope tensorielle cartésienne (x,y,z;x,y,z)
         REAL(KIND(0D0)), DIMENSION(1:3,1:3) :: conductivitet
         !> Résistivité anisotrope tensorielle cartésienne (x,y,z;x,y,z),
         !! calculée comme l'inverse de la conductivité anisotrope tensorielle
         REAL(KIND(0D0)), DIMENSION(1:3,1:3) :: resistivitet
         REAL(KIND(0D0))                 :: permeabiliteLineaire
         REAL(KIND(0D0)), DIMENSION(1:4) :: coefficientsMarrocco
         LOGICAL                         :: coordonneesPolaires
         TYPE(POINT)                     :: origineReperePolaire
         REAL(KIND(0D0)), DIMENSION(1:3) :: axe
         REAL(KIND(0D0))                 :: section
         INTEGER                         :: nbSpires
         REAL(KIND(0D0))                 :: norme
         !CHARACTER(LEN=40)               :: nomFamMED
         !> Nom du groupe de maillage pour librairie MED version 3 (med3.f90),
         !! de longueur MED_TAILLE_LNOM en principe, mais cela n'est pas important.
         CHARACTER(LEN=80)               :: nomGroupeMaillage
         !> Masse volumique du milieu, en unités S.I. kg/m3 (0 par défaut)
         real(kind(0d0)) :: masseVolumique
         !> Pertes (fer ou cuivre)
         type(typePertes) :: pertes
         !
         ! Définition et données du calcul amélioré de J dans un inducteur (Antoine Pierquin, L2EP)
         !
         !> Nom du fichier contenant les noeuds (points) de la ligne directrice pour définir la direction de J dans un milieu
         !! Si vide, par défaut, pas de ligne directrice définie, il faut préciser la direction de J de manière analytique (droit ou circulaire).
         !! Le format DAT de ce fichier est exporté par Salomé/Mesh.
         !! C'est  un paramètre de configuration par l'utilisateur
         CHARACTER(LEN=80) :: JligneDirectrice
         !> Noeuds de la ligne directrice lus dans le fichier JligneDirectrice
         REAL(KIND(0D0)), DIMENSION(:,:), ALLOCATABLE :: noeudsJ
         !> Arêtes de la ligne directrice lus dans le fichier JligneDirectrice
         INTEGER, DIMENSION(:,:), ALLOCATABLE :: aretesJ
       END TYPE

       !> Taille d'un milieu, en octets
       integer(kLong), parameter :: kTailleMILIEU = 12*kReal + 80*kCharacter + kLogical + kInteger&
                                                        + kTaillePOINT + kTailletypePertes

       !> Conditions limites sur les champs.
       TYPE CONDITIONSLIMITESCHAMPS
        !> Type de condition aux limites.
        !! Les types possibles sont : B.n=0, H.t=0, Jind.n=0, E.t=0, K.t=0, periodicite, periodicite+Ksymetrique, antiperiodicite, libre, antiperiodicite+Ksymetrique.
        CHARACTER(LEN=40) :: type
        INTEGER           :: GroupeNoeudIDEAS
        INTEGER           :: GroupeNoeudIDEAS2
        INTEGER           :: GroupePoints
        INTEGER           :: mouvementAssocie
        REAL(KIND(0D0)) :: angle
        !> axe de rotation choisi par l'utilisateur, ce qui remplace GroupePoints
        REAL(KIND(0D0)), DIMENSION(1:3) :: axe
        !> centre de rotation choisi par l'utilisateur, ce qui remplace GroupePoints
        REAL(KIND(0D0)), DIMENSION(1:3) :: centre
       END TYPE

       !> Taille du type CONDITIONSLIMITESCHAMPS, en octets
       integer(kLong), parameter :: kTailleCONDITIONSLIMITESCHAMPS = 40*kCharacter + 4*kInteger

       ! Prise en compte du mouvement
       ! - si translation vitesse est en m/s
       ! - si rotation vitesse est en tr/min
       ! - deltaMaillage indique le pas du maillage
       ! - deltaReel indique le pas de deplacmenet reel
       ! - surfaceGlissement indique la surface de glissement
       !   par un groupe de noeud d ideas
       ! - milieuGlissement indique le milieu associe
       !   a la surface de glissement
       ! - conditionPeriodiciteAssocie indique le condition de periodicite
       !   associe au mouvement definie dans les conditions limites.
       TYPE MOUVEMENT
        REAL(KIND(0D0)) :: deltaMaillage
        REAL(KIND(0D0)) :: deltaReel
        REAL(KIND(0D0)) :: signe
        INTEGER :: surfaceGlissement
        INTEGER :: milieuGlissement
        INTEGER, DIMENSION(:,:), ALLOCATABLE :: IndiceNoeudApermute
        INTEGER, DIMENSION(:,:), ALLOCATABLE :: IndiceAreteApermute
        INTEGER, DIMENSION(1:2) :: NombreNoeudApermute
        INTEGER, DIMENSION(1:2) :: NombreAreteApermute
        LOGICAL :: MvtLibre
        INTEGER :: nbPermutPas
        !> Axe de rotation de ce mouvement,
        !! défini comme un entier kAxeOX=1 pour l'axe OX, kAxeOY=2 pour l'axe OY, kAxeOZ=3 pour l'axe OZ (par défaut)
        !! Ceci n'est nécessaire que pour un mouvement de rotation bien sûr.
        integer :: axeRotation
       END TYPE

       !> Taille du type MOUVEMENT, en octets
       integer(kLong), parameter :: kTailleMOUVEMENT = 3*kReal + kLogical + 6*kInteger

       ! Variable test sur la distance entre deux noeuds.
       REAL(KIND(0D0)) ::  kEpsilonDistance
       REAL(KIND(0D0)) :: kdistanceRef

       ! element en contact avec la surface de glissement.
       ! utilise pour sauvegarde les coordonnees d'origines des elements.
       TYPE ELEMENTMOUVEMENT
         INTEGER :: indice
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement,1:3) :: coordonneesOriginales
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement) :: signeMvtLibre
       END TYPE

       !> Taille du type ELEMENTMOUVEMENT, en octets
       integer(kLong), parameter :: kTailleELEMENTMOUVEMENT = kInteger&
                        + (kNombreMaximumNoeudsElement*3 + kNombreMaximumAretesElement)*kInteger


       ! Conditions de periodicite ou anti periodicite.
       ! si periodicite alors periodique = .true.
       ! sinon anti periodicite, periodique = .false.
       ! si mouvement sans condition de periodicite alors
       ! MvtLibre = .true.
       ! groupes issus d ideas dans groupe1 et groupe2
       ! type defini si on effectue une transformation de
       ! translation (type=1) ou de rotation (type=2) entre les deux groupes.
       ! si translation, le vecteur sera defini.
       ! si rotation, l'axe et l'angle seront defini.
       TYPE CONDITION_PERIODICITE
        LOGICAL :: MvtLibre
        LOGICAL :: periodique
        LOGICAL :: Ksymetrique
        INTEGER :: type
        INTEGER :: groupe1,groupe2,groupePoints
        REAL(KIND(0D0)), DIMENSION(1:3) :: vecteur
        REAL(KIND(0D0)) :: angle
        REAL(KIND(0D0)) :: dangle
        ! axe de rotation choisi par l'utilisateur
        REAL(KIND(0D0)), DIMENSION(1:3) :: axe
        ! centre de rotation choisi par l'utilisateur
        REAL(KIND(0D0)), DIMENSION(1:3) :: centre
        REAL(KIND(0D0)), DIMENSION(1:3,1:3) :: matricePassage
        INTEGER :: mouvementAssocie
        !> Axe de rotation entre les deux surface de périodicité (usage interne),
        !! défini comme un entier kAxeOX=1 pour l'axe OX, kAxeOY=2 pour l'axe OY, kAxeOZ=3 pour l'axe OZ (par défaut)
        !! Ceci n'est nécessaire que pour une rotation bien sûr.
        !! L'axe est défini automatiquement en cas de mouvement de rotation associé.
        !! ATTENTION! Sans mouvement de rotation, l'axe est OZ mais il faudrait pouvoir le définir à la main.
        integer :: axeRotation
       END TYPE

       !> Taille du type CONDITION_PERIODICITE, en octets
       integer(kLong), parameter :: kTailleCONDITION_PERIODICITE = 3*kLogical + 6*kInteger + 13*kReal

       ! Type condition a la limite homogene. Une condition a la limite
       ! comprend:
       ! - un type de condition.
       TYPE CONDITION_LIMITE
         INTEGER :: nature
       END TYPE

       !> Taille du type CONDITION_LIMITE, en octets
       integer(kLong), parameter :: kTailleCONDITION_LIMITE = kInteger

       ! Type macro-groupe de noeuds. Un macro-groupe de noeuds comprend:
       ! - un flag indiquant si le macro-groupe refere des noeuds frontieres du
       !   domaine d'etude.
       ! - un indice de condition a la limite ("kNil" si le groupe n'est relie a
       !   aucune condition).
       ! - une liste booleene de groupe de noeuds unitaires.
       TYPE MGROUPE_NOEUDS
         LOGICAL :: frontiere
         INTEGER :: indiceConditionLimite
         LOGICAL, DIMENSION(1:knombreMaximumGroupes) :: listeGroupes
       END TYPE

       !> Taille du type MGROUPE_NOEUDS, en octets
       integer(kLong), parameter :: kTailleMGROUPE_NOEUDS = kInteger + (kNombreMaximumGroupes+1)*kLogical

       ! Type macro-groupe d'aretes. Un macro-groupe d'aretes comprend:
       ! - un flag indiquant si le macro-groupe refere des aretes frontieres du
       !   domaine d'etude.
       ! - un indice de condition a la limite ("kNil" si le groupe n'est relie a
       !   aucune condition).
       ! - une liste booleene de groupe d'aretes unitaires.
       TYPE MGROUPE_ARETES
         LOGICAL :: frontiere
         INTEGER :: indiceConditionLimite
         LOGICAL, DIMENSION(1:knombreMaximumGroupes) :: listeGroupes
       END TYPE

       !> Taille du type MGROUPE_ARETES, en octets
       integer(kLong), parameter :: kTailleMGROUPE_ARETES = kInteger + (kNombreMaximumGroupes+1)*kLogical

       ! Type macro-groupe de facettes. Un macro-groupe de facettes comprend:
       ! - un flag indiquant si le macro-groupe refere des facettes frontieres
       !   du domaine d'etude.
       ! - un indice de condition a la limite ("kNil" si le groupe n'est relie a
       !   aucune condition).
       ! - une liste booleene de groupe de facettes unitaires.
       TYPE MGROUPE_FACETTES
         LOGICAL :: frontiere
         INTEGER :: indiceConditionLimite
         LOGICAL, DIMENSION(1:knombreMaximumGroupes) :: listeGroupes
       END TYPE

       !> Taille du type MGROUPE_FACETTES, en octets
       integer(kLong), parameter :: kTailleMGROUPE_FACETTES = kInteger + (kNombreMaximumGroupes+1)*kLogical

       !> Type macro-groupe de milieux (ou d'elements). Un macro-groupe de
       !! milieux comprend:
       !! - un indice de condition a la limite ("kNil" si le groupe n'est relie a
       !!   aucune condition).
       !! - une liste booleene de milieux.
       TYPE MGROUPE_MILIEUX
         INTEGER :: indiceConditionLimite
         LOGICAL, DIMENSION(1:kNombreMaximumMilieux) :: listeGroupes
       END TYPE

       !> Taille du type MGROUPE_MILIEUX, en octets
       integer(kLong), parameter :: kTailleMGROUPE_MILIEUX = kInteger + kNombreMaximumMilieux*kLogical


       ! Type inducteur. Un inducteur comprend:
       ! - un macro-groupe de milieux.
       ! - le courant associe a l inducteur.
       ! - le courant associe a l inducteur a l instant precedent
       ! - la tension associe a l inducteur.
       ! - la tension associe a l inducteur a l instant precedent.
       ! - la resistance associe a l inducteur.
       ! - flag logical indiquant si l inducteur est couple.
       ! - l inconnue associe a l inducteur.
       ! - la valeur du flux a l instant precedent
       ! - surfaces entree et sortie sont utilisée pour un inducteur massif
       !   a tension ou courant imposé.
       ! - type = 1 si inducteur bobine alimente courant
       !          2 si inducteur bobine alimente tension
       !          3 si inducteur massif alimente courant
       !          4 si inducteur massif alimente tension
       !          5 si bobine alimenter tension qucs
       !          6 si inducteur massif couplé qucs
       ! - nnNoeudsCoupure nombre de noeuds de la coupure.
       TYPE INDUCTEUR
         TYPE(MGROUPE_MILIEUX) :: mGroupeMilieux
         REAL(KIND(0D0)) :: courant
         REAL(KIND(0D0)) :: courantPrecedent
         REAL(KIND(0D0)) :: tension
         REAL(KIND(0D0)) :: tensionPrecedente
         REAL(KIND(0D0)) :: fluxMagn
         REAL(KIND(0D0)) :: resistance
         REAL(KIND(0D0)) :: inductance
         INTEGER         :: inconnue
         REAL(KIND(0D0)) :: fluxMagnPrecedent
         REAL(KIND(0D0)) :: fluxMagnCouple
         REAL(KIND(0D0)) :: fluxMagnCoupleNL
         INTEGER         :: type
         INTEGER         :: surfaceEntree,surfaceSortie
         REAL(KIND(0D0)) :: sectionEntree,sectionSortie
         INTEGER, DIMENSION(1:knombreMaximumEleGeoCoupure) :: listeNoeudsCoupure
         INTEGER         :: nbNoeudsCoupure
         REAL(KIND(0D0)) :: perteJoule
         LOGICAL         :: couplageTension
         character(len=30)  :: nomqucs
         integer            :: indicedipolequcs
         !> Calcul de la densité de courant J par la méthode de minimisation (.true.)
         !! ou par la méthode classique de l'arbre (.false.).
         !! C'est un paramètre utilisateur
         logical :: Jminimisation
         !> Nombre d'itérations maximales de l'algorithme de minimisation
         integer :: JminNbIterationsMax
         !> Précision demandée sur la solution à l'algorithme de minimisation
         real(kind(0D0)) :: JminPrecisionSolution
         !> Précision demandée sur le résidu à l'algorithme de minimisation
         real(kind(0D0)) :: JminPrecisionResidu
         !> Au moins un des morceaux composant l'inducteur utilise une ligne directrice pour calculer le flux de J à travers les faces (.true.)
         !! ou aucun (.false.).
         !! Dans ce dernier cas, l'algorithme d'arbre classique est utilisé, si la minimisation n'est pas requise.
         !! C'est un paramètre interne.
         logical :: JligneDirectrice

       END TYPE

       !> Taille du type INDUCTEUR, en octets
       integer(kLong), parameter :: kTailleINDUCTEUR = kTailleMGROUPE_MILIEUX + 13*kReal &
                                                        + (5+knombreMaximumEleGeoCoupure)*kInteger + kLogical

       ! Type TermeSourceMagn.
       ! - le flux magn associe .
       ! - le flux magn a l instant precedent
       ! - la ddp magn associee.
       ! - la ddp magn a l instant precedent.
       ! - flag logical indiquant si le terme source magn est couple.
       ! - l inconnue associe au terme source.
       ! - surfaces entree et sortie.
       ! - type = 1 si le flux magn est impose
       !          2 si la ddp magn est imposee
       ! - nombre de noeuds de la coupure.
       TYPE TERMESOURCEMAGN
         TYPE(MGROUPE_MILIEUX) :: mGroupeMilieux
         REAL(KIND(0D0)) :: fluxMagn
         REAL(KIND(0D0)) :: ddpMagn
         REAL(KIND(0D0)) :: fluxMagnPrecedent
         REAL(KIND(0D0)) :: ddpMagnPrecedent
         INTEGER         :: inconnue
         REAL(KIND(0D0)) :: fluxMagnCouple
         REAL(KIND(0D0)) :: fluxMagnCoupleNL
         INTEGER         :: type
         INTEGER         :: surfaceEntree,surfaceSortie
         REAL(KIND(0D0)) :: sectionEntree,sectionSortie
         INTEGER, DIMENSION(1:knombreMaximumEleGeoCoupure) :: listeNoeudsCoupure
         INTEGER         :: nbNoeudsCoupure
         LOGICAL         :: couplageDDPmagnetique
       END TYPE

       !> Taille du type TERMESOURCEMAGN, en octets
       integer(kLong), parameter :: kTailleTERMESOURCEMAGN = kTailleMGROUPE_MILIEUX + 8*kReal &
                                                        + (5+knombreMaximumEleGeoCoupure)*kInteger + kLogical

       ! Type aimant. Un aimant comprend:
       ! - un macro-groupe de milieux.
       TYPE AIMANT_TYPE
         TYPE(MGROUPE_MILIEUX) :: mGroupeMilieux
       END TYPE

       !> Taille du type AIMANT_TYPE, en octets
       integer(kLong), parameter :: kTailleAIMANT = kTailleMGROUPE_MILIEUX

       ! Type systeme morse. Un systeme morse comprend:
       ! - une matrice ligne A.
       ! - la liste des indices de colonnes jA.
       ! - la liste des debuts de lignes iA.
       ! - le second membre B.
       ! - le vecteur inconnu predX a l'iteration precedente.
       ! - le vecteur inconnu predXNonLineaire a l'iteration precedente
       !   dans la boucle de saturation.
       ! - le vecteur inconnu X a l'iteration courante.
       ! - la taille de la matrice A.
       ! - le nombre d'inconnues.
       ! - l'indice correspondant a la premiere inconnue nodale dans les
       !   vecteur B, X, predX et predXNonLineaire. Les inconnues aretes
       !   sont rangees devant les inconnues nodales.
       TYPE SYSTEME_MORSE
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: A
          INTEGER, DIMENSION(:), ALLOCATABLE         :: jA
          INTEGER, DIMENSION(:), ALLOCATABLE         :: iA
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: B
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: BsourceDyn
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: BsourceSta
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: predX
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: predBsourceDyn
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: predBDyn
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: predXNonLineaire
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: X
          INTEGER :: tailleSysteme
          INTEGER :: nombreInconnues
          INTEGER :: nombreInconnuesArete
          INTEGER :: nombreInconnuesNodale
          INTEGER :: nombreInconnuesElectrique
          INTEGER :: nombreInconnuesMagnetique
          INTEGER :: nombreInconnuesMailles
          INTEGER :: premierNumeroInconnueNodale
          !> Variable decrivant l'etat du solveur lineaire
          !! pour gerer la mutualisation dans la resolution non lineaire.
          !!
          !! True: on peut faire un solve (call resolutionsysteme('SOLVE'...)) ou un 'CLEAN'
          !!
          !! False: on peut faire seulement 'INIT' ou 'ALL'
          LOGICAL :: Lmutualization_sl
          !
          ! Variables gerant le fonctionnement de MUMPS pour la resolution de CE systeme lineaire
          !
#if USE_MUMPS==1
          !> Instance MUMPS simple précision.
          !! Disponible seulement si la librairie MUMPS est installée
          type(smumps_struc) :: id_smumps
          !> Instance MUMPS double précision.
          !! Disponible seulement si la librairie MUMPS est installée
          type(dmumps_struc) :: id_dmumps
#endif
          !> Timers pour monitoring etapes MUMPS
          REAL(KIND(0D0)) :: time_cpu(3),time_elapsed(3)
          !> Vecteur de parametres réels carmel pour piloter MUMPS
          REAL(KIND(0D0)) :: mumps_cntl(15)
          !> Vecteur de parametres entiers carmel pour piloter MUMPS
          INTEGER :: mumps_icntl(40)
          !> T: solveur direct, F: preconditionneur
          LOGICAL :: Lmumps_double
          !> Activation de l'occurence MUMPS liee a CE systeme Carmel
          LOGICAL :: Lmumps_initialize
          !> matrice de precond si CROUT
          REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: C
          !
       END TYPE

       !> Taille du type SYSTEME_MORSE, en octets (statique)
       !! Attention! On ne tient pas compte de la taille des types des structures MUMPS
       integer(kLong), parameter :: kTailleSYSTEME_MORSE = 47*kInteger + 3*kLogical + 21*kReal

       !> Construction des matrices et sources élémentaires en parallèle ou lors de la séparation des contributions linéaire et non-linéaire
       ! Ses paramètres sont utilisés dans la routine contributionHsOmega (formulationHsOmega.f90) en // OpenMP
       type typeContributionElement
            !>
            INTEGER :: erreur
            !> Matrice de couplage tension pour les inducteurs.
            !! On note qu'elle n'est pas dédiée à un élément, car globale pour tout le système.
            REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInducteurs, 1:kNombreMaximumInducteurs) :: C
            !> Matrice de raideur d'un element.
            REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement,1:kNombreMaximumInconnuesElement) :: A
            !> Terme source d'un element en statique.
            REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement) :: BsourceSta
            !> Terme source d'un element en dynamique
            REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement) :: BsourceDyn
            !> Terme precedent dynamique.
            REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement) :: predBdyn
            !> Numeros d'inconnues d'un element.
            INTEGER, DIMENSION(1:kNombreMaximumInconnuesElement ) :: numerosInconnues
            !> Nombre d'inconnues d'une matrice elementaire.
            INTEGER :: nbInconElem
            !> Flag indiquant si l'element courant se trouve dans un milieu non lineaire.
            LOGICAL :: flagLinearite
            !> Contribution d'un élément linéaire à la matrice Grad x Mu x Grad
            !REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement,1:kNombreMaximumNoeudsElement):: gradMuGrad
            !> Contribution d'un élément linéaire à la matrice Rot x Rot.
            !REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement,1:kNombreMaximumAretesElement) :: ST
            !> Contribution d'un élément linéaire à la matrice Grad x Mu x Grad (bis)
            !REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement,1:kNombreMaximumNoeudsElement) :: SOmega
            !> Contribution d'un élément linéaire à la matrice ?
            !REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement,1:kNombreMaximumAretesElement) :: tCTOmega
            !> Contribution d'un élément linéaire à la matrice ?
            !REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement,1:kNombreMaximumNoeudsElement) :: CTOmega
            !> Contribution d'un élément linéaire à la matrice ?
            !REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement,1:kNombreMaximumAretesElement) :: T
            !> Contribution d'un élément linéaire à la partie de la matrice A qui fournit la source BsourceSta une fois multipliée par predXNonLineaire
            REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement,1:kNombreMaximumInconnuesElement) :: &
                A_predXNonLineaire_BsourceSta
            !> Contribution d'un élément linéaire à la partie de la matrice A qui fournit la source predBDyn une fois multipliée par predXNonLineaire
            REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement,1:kNombreMaximumInconnuesElement) :: &
                A_predXNonLineaire_predBDyn
            !> Nombre de noeuds de l'élément
            !INTEGER :: nbNoeuds
            !> Nombre d'arêtes de l'élément
            !INTEGER :: nbAretes
            !> Nombre d'inconnues de l'élément (répétition avec nbInconElem ?)
            !INTEGER :: nbInconnues
       end type

       !> Taille du type typeContributionElement, en octets (statique) : à mettre à jour
       integer(kLong), parameter :: kTailleTypeContributionElement = kLogical + (4+kNombreMaximumInconnuesElement)* kInteger &
        + kReal * (kNombreMaximumInducteurs**2 + kNombreMaximumInconnuesElement**2 + 3*kNombreMaximumInconnuesElement &
        + kNombreMaximumNoeudsElement**2)

       ! Type matrice_milieu
       ! Permet identifier quel type de matrice est associée à un groupe d element (milieux)
       TYPE MATRICE_GROUPE
         TYPE(MGROUPE_MILIEUX) :: RotRot
         TYPE(MGROUPE_MILIEUX) :: GradGrad
         TYPE(MGROUPE_MILIEUX) :: W1Grad
         TYPE(MGROUPE_MILIEUX) :: W1W1
       END TYPE

       !> Taille du type MATRICE_GROUPE, en octets
       integer(kLong), parameter :: kTailleMATRICE_GROUPE = 4*kTailleMGROUPE_MILIEUX


       ! Permet identifier quelle loi de  comportement est associée à un groupe d element (milieux)
       TYPE LOI_COMPORTEMENT_GROUPE
         TYPE(MGROUPE_MILIEUX) :: permeabilitelineaire   ! si false donc non lineaire
         TYPE(MGROUPE_MILIEUX) :: permeabiliteisotrope   ! si false donc non anisotrope
         TYPE(MGROUPE_MILIEUX) :: conductivitelineaire   ! si false donc non lineaire
         TYPE(MGROUPE_MILIEUX) :: conductiviteisotrope   ! si false donc non anisotrope
       END TYPE

       !> Taille du type LOI_COMPORTEMENT_GROUPE, en octets
       integer(kLong), parameter :: kTailleLOI_COMPORTEMENT_GROUPE = 4*kTailleMGROUPE_MILIEUX

       !  Permet identifier les termes sources associée à un groupe d element (milieux) ou de noeuds
       TYPE CHAMP_VECTEUR_SOURCE
         TYPE(MGROUPE_MILIEUX) :: N
         TYPE(MGROUPE_MILIEUX) :: K
         TYPE(MGROUPE_NOEUDS)  :: alpha
       END TYPE

       !> Taille du type CHAMP_VECTEUR_SOURCE, en octets
       integer(kLong), parameter :: kTailleCHAMP_VECTEUR_SOURCE = 2*kTailleMGROUPE_MILIEUX + kTailleMGROUPE_NOEUDS

       TYPE NATURE_ASSEMBLER
            TYPE(MATRICE_GROUPE)          :: Matrice
            TYPE(LOI_COMPORTEMENT_GROUPE) :: Loi
            TYPE(CHAMP_VECTEUR_SOURCE)    :: ChampVecteur
       END TYPE

       !> Taille du type NATURE_ASSEMBLER, en octets
       integer(kLong), parameter :: kTailleNATURE_ASSEMBLER = kTailleMATRICE_GROUPE &
                            + kTailleLOI_COMPORTEMENT_GROUPE + kTailleCHAMP_VECTEUR_SOURCE

       TYPE Grandeur_Globale
           LOGICAL,DIMENSION(:), ALLOCATABLE :: CourantImpose
       END TYPE

       !> Type sous_systeme morse. Un systeme morse comprend:
       !! - Flag logical symetrique
       !! - une matrice ligne A.
       !! - le vecteur source B
       !! - le vecteur solution X
       !! - la liste des indices de colonnes jA.
       !! - la liste des debuts de lignes iA.
       !
       TYPE SOUS_SYSTEME_MORSE
         LOGICAL :: symetrique
         INTEGER,         DIMENSION(:), ALLOCATABLE :: jA
         INTEGER,         DIMENSION(:), ALLOCATABLE :: iA
         REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: A
         REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: B
         REAL(KIND(0D0)), DIMENSION(:), ALLOCATABLE :: X
         INTEGER :: tailleSysteme
         INTEGER :: nombreInconnues
       END TYPE

       !> Taille du type SOUS_SYSTEME_MORSE, en octets (statique)
       integer(kLong), parameter :: kTailleSOUS_SYSTEME_MORSE = 2*kInteger + kLogical

       !> Matrice au format creux CSR, d'usage plus général que les types SYSTEME_MORSE ou SOUS_SYSTEME_MORSE
       !! @author: Guillaume CARON (L2EP)
       type Stockage_csr
            !> vecteur composé des coeff non nul (NNZ)
            !! qui est la valeur (A) dans un type SYSTEME_MORSE ou SOUS_SYSTEME_MORSE
            REAL(kind(0D0)), dimension(:), allocatable :: Acoeff
            !> vecteur composé des indices col des NNZ,
            !! qui est nommée jA dans un type SYSTEME_MORSE ou SOUS_SYSTEME_MORSE
            INTEGER, dimension(:), allocatable :: Aj
            !> vecteur composé du premier NNZ de chaque ligne,
            !! qui est nommée iA dans un type SYSTEME_MORSE ou SOUS_SYSTEME_MORSE
            INTEGER, dimension(:), allocatable :: Ai
            !> Nb d'éléments non nuls (NoNe Zero element),
            !! qui est la taille du système (tailleSysteme) dans un type SYSTEME_MORSE ou SOUS_SYSTEME_MORSE
            INTEGER :: NNZ
            !> Nb de lignes de la matrice,
            !! qui est le nombre d'inconnues (nombreInconnues) dans un type SYSTEME_MORSE ou SOUS_SYSTEME_MORSE
            INTEGER :: NbRow
            !> Pour les matrices symétriques, renseigne si le stockage est triangulaire (1 triang, 0 non)
            LOGICAL :: symetrique
       end type

       !> Taille du type Stockage_csr, en octets (statique)
       integer(kLong), parameter :: kTailleStockage_csr = 2*kInteger + kLogical

       !> Type informations sur un element.
       !! Cf. ci-dessous pour avoir la liste de ce que ces informations comprennent.
       !
       TYPE INFOS_ELEMENT
         !> Indice de l'element.
         INTEGER :: indiceElement
         !> Indice de l'element de reference.
         INTEGER :: iref
         !> Indice de milieu.
         INTEGER :: indiceMilieu
         !> Indice dans le maillage, important pour MED.
         INTEGER :: indiceMaillage
         !> Nombre de noeuds.
         INTEGER :: nbNoeuds
         !> Nombre d'aretes.
         INTEGER :: nbAretes
         !> Nombre de facettes.
         INTEGER :: nbFacettes
         !> Nombre d'inconnues.
         INTEGER :: nbInconnues
         !> Nombre de points de Gauss (quadrature).
         INTEGER :: nbPoint
         !> Coordonnees des noeuds.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement,1:3) :: coordonnees
         !> Circulations du champ H sur les aretes.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement) :: circulationsH
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement) :: circulationsPredH
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement) :: circulationsHNorm
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumAretesElement,1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn)&
                                                                   :: circulationsHsNormalise

         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumFacettesElement,1:kNombreMaximumInducteurs+kNombreMaximumSourceMagn)&
                                                                   ::  fluxJNormalise
         !> Flux de J sur les facettes.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumFacettesElement) :: fluxJ
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumFacettesElement) :: fluxJNorm
         !> Flux de J sur les facettes a l'instant Precedent.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumFacettesElement) :: fluxJPred
         !> Liste des numeros d'inconnues.
         INTEGER, DIMENSION(1:kNombreMaximumInconnuesElement) :: numerosInconnues
         !> Inconnues a l'iteration precedente dans la boucle de temps.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement) :: predX
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement) :: predB
         !> Inconnues a l'iteration precedente dans la boucle de saturation.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement) :: predXNonLineaire
         !> Inconnues a l'iteration courante.
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumInconnuesElement) :: X
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement) :: PhiSource
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement) :: predPhiSource
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement) :: V
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement) :: OmegaSource
         REAL(KIND(0D0)), DIMENSION(1:kNombreMaximumNoeudsElement) :: predOmegaSource
         !> Volume de l'élément (m^3)
         real(kind(0d0)) :: volume
         !> Coordonnees cartésiennes (x,y,z) du centre de l'élément de référence.
         REAL(KIND(0D0)), DIMENSION(1:3) :: centreRef
       END TYPE

       !> Taille du type INFOS_ELEMENT, en octets
       integer(kLong), parameter :: kTailleINFOS_ELEMENT = (9+kNombreMaximumInconnuesElement)*kInteger &
        + kReal*(kNombreMaximumNoeudsElement*8+kNombreMaximumAretesElement*(3+kNombreMaximumInducteurs+kNombreMaximumSourceMagn)&
                 +kNombreMaximumFacettesElement*(3+kNombreMaximumInducteurs+kNombreMaximumSourceMagn)&
                 +kNombreMaximumInconnuesElement*4)

       ! Type AttributNoeud :
       ! - numero du noeud
       ! - nombre d aretes connectees au noeud
       TYPE AttributNoeud
        INTEGER :: NumeroNoeud
        INTEGER :: NombreAreteNoeud
       END TYPE

       !> Taille du type AttributNoeud, en octets
       integer(kLong), parameter :: kTailleAttributNoeud = 2*kInteger

       ! Type spire exploratrice :
       ! - associe un macro groupe de noeuds.
       ! - liste des indices des aretes formant la spire exploratrice.
       ! - liste des orientations des aretes formant la spire exploratrice.
       ! - nombre d aretes formant la spire
       ! - attribut noeud (pour la suppression d aretes en trop)
       ! - Valeur du flux en B
       ! - Valeur du flux en A
       TYPE SpireExploratrice
        LOGICAL, DIMENSION(1:knombreMaximumGroupes)                   :: listeGroupes
        INTEGER, DIMENSION(1:knombreMaximumAretesSpire)               :: listeAreteSpire
        INTEGER, DIMENSION(1:knombreMaximumAretesSpire)               :: listeOrientationAreteSpire
        INTEGER                                                       :: nombreAreteSpire
        INTEGER                                                       :: nombreNoeudSpire
        TYPE (AttributNoeud),DIMENSION(1:knombreMaximumAretesSpire*2) :: attributNoeud
        REAL(KIND(0D0))                                               :: fluxB
        REAL(KIND(0D0))                                               :: fluxA
       END TYPE

       !> Taille du type SpireExploratrice, en octets
       integer(kLong), parameter :: kTailleSpireExploratrice = knombreMaximumGroupes*kLogical &
        + (2*knombreMaximumAretesSpire+2)*kInteger + knombreMaximumAretesSpire*2*kTailleAttributNoeud + 2*kReal

       ! Type GroupeElementFlux
       ! - donne le numero du milieu correspondant a un groupe de noeuds.
       ! - donne la liste des noeuds associe au groupes.
       ! - valeur du flux
       TYPE GroupeElementFlux
        INTEGER                                                       :: numeroMilieu
        LOGICAL, DIMENSION(1:knombreMaximumGroupes)                   :: listeGroupes
        REAL(KIND(0D0))                                               :: flux
       END TYPE

       !> Taille du type GroupeElementFlux, en octets
       integer(kLong), parameter :: kTailleGroupeElementFlux = kInteger + knombreMaximumGroupes*kLogical + kReal

       ! Type Coupure :
       ! - numero de l inconnue associee a la coupure.
       ! - indique les deux groupes pour la formation de la coupure.
       ! - liste des indices des elements geometriques (noeud ou arete) formant la coupure.
       ! - liste des orientations des elements geometriques formant la coupure.
       ! - nombre d elements geometriques de la coupure
       ! - elements de la coupure, utiliser en omega
       ! - nombre d elements de la coupure, utiliser en omega
       ! - type de coupure : 1 en omega, 2 en A.
       ! - nature de coupure : 1 inconnue, 2 source.
       ! - en A valeurSouce est un flux, en Omega une fmm.
       TYPE coupure
        INTEGER                                                     :: numeroInconnue
        LOGICAL, DIMENSION(1:knombreMaximumGroupes)                 :: listeGroupes1
        LOGICAL, DIMENSION(1:knombreMaximumGroupes)                 :: listeGroupes2
        INTEGER, DIMENSION(1:knombreMaximumEleGeoCoupure)           :: listeEleGeoCoupure
        INTEGER, DIMENSION(1:knombreMaximumEleGeoCoupure)           :: listeOrientationEleGeoCoupure
        INTEGER, DIMENSION(1:knombreMaximumElementCoupure)          :: listeElementCoupure
        INTEGER                                                     :: nombreEleGeoCoupure
        INTEGER                                                     :: nombreElementCoupure
        INTEGER                                                     :: typeCoupure
        INTEGER                                                     :: natureCoupure
        REAL(KIND(0D0))                                             :: valeurSource
        REAL(KIND(0D0))                                             :: predvaleurSource
        REAL(KIND(0D0))                                             :: fluxJinduit
       END TYPE

       !> Taille du type coupure, en octets
       integer(kLong), parameter :: kTaillecoupure = kInteger &
        + (2*knombreMaximumGroupes+2*knombreMaximumEleGeoCoupure+knombreMaximumElementCoupure)*kLogical &
        + 4*kInteger + 3*kReal

       !> TYPE ForceCouple
       !! - milieu de calcul de la force.
       !! - groupe de noeud ou l'on calcule la force
       !! - force totale
       !! - couple
       TYPE ForceCouple
         TYPE(MGROUPE_MILIEUX)    ::  MilieuForceCouple
         LOGICAL, DIMENSION(1:knombreMaximumGroupes)  ::  GroupeNoeudsForce
         REAL(KIND(0D0)), DIMENSION(1:3) :: force
         REAL(KIND(0D0)) :: couple
       END TYPE
       !> Taille du type ForceCouple
       integer(kLong), parameter :: kTailleForceCouple = 4*kReal + kNombreMaximumGroupes*kLogical + kTailleMGROUPE_MILIEUX

       ! Type potentiel flottant.
       ! - groupe de noeuds ayant la meme inconnue.
       ! - numero inconnue associee.
       TYPE POTENTIELFLOTTANT
         INTEGER :: GroupeNoeudIDEAS
         INTEGER :: numeroInconnue
         REAL(KIND(0D0)) :: valeur
       END TYPE

       !> Taille du type POTENTIELFLOTTANT, en octets
       integer(kLong), parameter :: kTaillePOTENTIELFLOTTANT = 2*kInteger + kReal

       !> Type donnee,
       !! qui permet de definir les fichiers de sortie, de formats différents,
       !! pour les grandeurs globales, les cartes de champs, les points explorateurs.
       !! Cf. ci-dessous pour connaître son contenu.
       TYPE donnees
        !> Flux des inducteurs (.true.),
        !! au format texte de nom fluxInd<NUMERO_INDUCTEUR><FORMULATION>.don,
        !! i.e., un fichier par inducteur et par formulation
        LOGICAL     :: fluxInducteur
        !> Energie du systeme (.true.),
        !! au format texte de nom energie_<FORMULATION>.don,
        !! i.e., un fichier par formulation
        LOGICAL     :: energie
        !> Courants dans les inducteurs (.true.),
        !! au format texte de nom courantInd<NUMERO_INDUCTEUR><FORMULATION>.don,
        !! i.e., un fichier par inducteur et par formulation
        LOGICAL     :: courantInducteur
        !> Tensions dans les inducteurs (.true.),
        !! au format texte de nom femInd<NUMERO_INDUCTEUR><FORMULATION>.don,
        !! i.e., un fichier par inducteur et par formulation
        LOGICAL     :: tensionInducteur
        !> Flux par les groupes (.true.),
        !! au format texte de nom fluxGroupe<NUMERO_GROUPE><FORMULATION>.don,
        !! i.e., un fichier par groupe et par formulation
        LOGICAL     :: fluxGroupe
        !> Flux des spires exploratrices (.true.),
        !! au format texte de nom fluxSpire<NUMERO_SPIRE><FORMULATION>.don,
        !! i.e., un fichier par spire et par formulation
        LOGICAL     :: fluxSpire
        !> Pertes Joule (.true.),
        !! au format texte de nom perteJoules<FORMULATION>.don,
        !! i.e., un fichier par formulation
        LOGICAL     :: perteJoule
        !> Différences de potentiel (d.d.p.) électrique (.true.),
        !! NON UTILISE
        LOGICAL     :: ddpElect
        !> Différences de potentiel (d.d.p.) magnétique par terme source magnétique (.true.),
        !! au format texte de nom ddpMagnTermeMagn<NUMERO_TERME_SOURCE_MAGNETIQUE><FORMULATION>.don,
        !! i.e., un fichier par terme source magnétique et par formulation
        LOGICAL     :: ddpMagn
        !> Flux magnétique par terme source magnétique (.true.),
        !! au format texte de nom FluxMagnTermeMagn<NUMERO_TERME_SOURCE_MAGNETIQUE><FORMULATION>.don,
        !! i.e., un fichier par terme source magnétique et par formulation
        LOGICAL     :: fluxMagn
        !> Flux total des courants induits (.true.),
        !! NON UTILISE
        LOGICAL     :: fluxJinduitTotal
        !> Force et couple par terme défini (.true.),
        !! au format texte de nom forceCouple<NUMERO_TERME><FORMULATION>.don,
        !! i.e., un fichier par terme défini et par formulation
        LOGICAL     :: forceCouple
        !> Valeur du potentiel flottant par potentiel flottant défini (.true.),
        !! au format texte de nom potFlottant<NUMERO_TERME><FORMULATION>.don,
        !! i.e., un fichier par potentiel flottant et par formulation
        LOGICAL     :: potFlottant
        ! format carte
        !> Liste des cartes de champs sauvegardés dans un fichier si .true.,
        !! e.g., le champ B est sauvegardé lorsque carteChamps( kChampB ) = .true.,
        !! e.g., tous les champs sont sélectionnés lorsque carteChamps(:) = .true.,
        !! e.g., les champs B et H sont sélectionnés lorsque carteChamps( (/kChampB, kChampH/) ) = .true.
        !! Les formats de sortie, pour tous les champs à la fois, sont définis par la variable carteChampFormats.
        LOGICAL, DIMENSION(1:kNombreMaximumChamps) :: carteChamps
        !> Liste des formats de sortie (MED, UNV, CSV), activés si .true., pour les cartes de champs.
        !! Une sortie sélectionnée sera appliquée à tous les champs activés (cf. carteChamps).
        !! Pour choisir un format, les indices de cette liste correspondent aux paramètres kMED, kUNV, kCSV,
        !! e.g., le format MED est activé si carteChampFormats( kMED ) = .true.,
        !! e.g., tous les formats sont activés si carteChampFormats(:) = .true.,
        !! e.g., les formats MED et CSV sont activés si carteChampFormats( (/kMED, kCSV/) ) = .true..
        LOGICAL, DIMENSION(1:kNombreMaximumFormats) :: carteChampsFormats
        !> Liste des milieux sur lesquels chaque champ est sorti sous forme de carte (cf. carteChamps).
        !! Ce filtrage, i.e., la liste des groupes, n'est disponible que pour le format CSV,
        !! car ce filtrage peut être réalisé par d'autres outils pour les autres formats.
        !! Ce tableau a deux dimensions : (liste des champs, liste des milieux), e.g., carteChampGroupes( kChampB, (/3, 4/) ).
        !! Les milieux sont identifiés par leur indice interne, et pas par l'indice du groupe de maillage (cf. la fonction indiceGroupe).
        LOGICAL, DIMENSION(1:kNombreMaximumChamps, 1:kNombreMaximumGroupes) :: carteChampsGroupes
        !> Liste des éléments ou des noeuds du maillage sur lesquels chaque champ est sorti sous forme de carte (cf. carteChamps).
        !! Ce filtrage, i.e., la liste des éléments ou des noeuds, n'est disponible que pour le format CSV,
        !! car ce filtrage peut être réalisé par d'autres outils pour les autres formats.
        !! Ce tableau a deux dimensions : (liste des champs, liste des éléments ou noeuds), e.g., carteChampElements( kChampB, 1 ) = 170000.
        !! Les éléments et les noeuds sont identifiés par leur label (indice du maillage).
        !! Le nombre d'éléments ou noeuds explorateurs possible est très limité,
        !!  car en pratique ce ne sont que quelques éléments qui intéressent l'utilisateur.
        integer, dimension(:,:), allocatable :: carteChampsElements
        !> Paramètres de post-traitement des pertes (Fer ou Cuivre)
        type(typePertesPost) :: pertes
        !> Choix du calcul de la valeur de la carte du champ dans un élément (mode expert).
        !! Une seule valeur est définie par élément, à partir des valeurs aux points de Gauss,
        !! e.g., valeur au dernier point de Gauss, moyenne sur les points de Gauss, pondérée ou non.
        !! Les choix possibles sont :
        !! - 'moyenne' : moyenne sur les points de Gauss, par défaut à partir de la version 1.13.0,
        !! - 'dernier' : valeur du dernier point de Gauss. Choix historique dans les versions 1.6.2 à 1.8.3 inclus,
        !! - 'version <= 1.8.3' : comportement des versions 1.8.3 et antérieures du code, i.e., dernier point de Gauss.
        !! - '1.9.0 <= version <= 1.12.2' : comportement des versions 1.9.0 à 1.12.2 inclus, i.e.,
        !!    dernier point de Gauss pour les tétraèdres, et valeur moyenne pour les prismes et hexaèdres.
        !!    Cas particulier du champ Jinduit, au dernier point de Gauss pour tous les types d'éléments.
        !!    Pour les prismes, la pondération utilisée est constante (1/6) et normée, ce qui équivaut à la valeur moyenne.
        character(len=26) :: carteChampsValeurElement
        !> Lors du calcul du post-traitement en programmation parallèle (OpenMP),
        !! le travail sur chaque thead (coeur, processeur) est réparti en paquets,
        !! qui sont des tranches sur tous les éléments.
        !! Ce paramètre utilisateur permet de définir le nombre de ces paquets par thread.
        !! La valeur 1, par défaut, permet de fournir le même nombre d'éléments à chaque thread,
        !!  mais cela peut ne pas être optimal lorsque le temps de post-traitement est très différent d'un élément à l'autre,
        !!  e.g., pour un problème non-linéaire avec mélange entre éléments linéaires (rapides à calculer),
        !!  et éléments non-linéaires (longs à calculer).
        !! Des valeurs de 10 à 1000 sont alors conseillées,
        !!  ce qui permet de mieux répartir le calcul et faire en sorte que les threads n'attendent pas pour rien.
        integer :: nbPaquetsThread
       END TYPE

       !> Taille du type donnees, en octets
       integer(kLong), parameter :: kTailledonnees = kTailletypePertesPost &
        + kLogical*(13+kNombreMaximumFormats+kNombreMaximumChamps*kNombreMaximumGroupes) &
        + kNombreMaximumChamps*(kInteger+kLogical) + kInteger

       !> Type pointExplorateurChamps dédié au calcul, en post-traitement, de valeurs des champs en différents points du maillage.
       !! Cf. ci-dessous pour connaître son contenu.
       !! Plusieurs points explorateurs peuvent être définis dans ce type, e.g., une ligne de coupe.
       !
       TYPE pointsExplorateursChamps_type
        !> Type, i.e., manière pour l'utilisateur pour construire la liste des points explorateurs.
        !! Les choix possibles sont :
        !! - 'segment' : segment (ligne droite entre 2 points),
        !!   défini par 2 points (point1, point2) et le nombre de points explorateurs (n) régulièrement répartis entre ces 2 points extrêmes,
        !! - 'fichier' : fichier contenant les coordonnées des points explorateurs demandés ou les indices du maillage des éléments demandés,
        !! - 'rectangle' : rectangle défini par les 3 points point1, point2 et point3.
        !!   Ces points forment le triangle dans l'espace qui va donner naissance au parallélépipède rectangle.
        !!   Ce rectangle n'est pas forcément droit.
        !!   Le nombre de points du rectangle est donné par le nombre de points entre point1 et point2 (n12)
        !!    et le nombre de points entre point1 et point3 (n13).
        !! - 'cercle' : cercle défini par un axe de rotation, un centre de rotation, un point de départ sur le cercle, et le nombre de points (n) sur le cercle,
        !! - 'arc' : arc de cercle défini par un axe de rotation, un centre de rotation, un point de départ sur l'arc, un angle total de l'arc, et le nombre de points (n) sur l'arc.
        !! - 'cylindre' : cylindre défini à l'instar du cercle, qui définit la base du cylindre, avec les
        !!      données supplémentaires de la longueur du cylindre (longueur) et le nombre de points
        !!      (nl) le long du cylindre. Une longueur positive formera un cylindre de la base étendu
        !!      en direction de l'axe. La longueur peut être négative pour former un cylindre, toujours
        !!        à partir de la base mais dans l'autre sens.
        character(len=50) :: type
        !> Champs pour lesquels ces points explorateurs sont recherchés.
        !! On précise la liste des champs voulus (.true.) ou non (.false., par défaut)
        !! en utilisant les valeurs habituelles des cartes de champ.
        !! Cette liste s'étend par construction sur tous les champs disponibles,
        !! mais seuls les champ B, H et J sont possibles en pratique.
        logical, dimension(1:kNombreMaximumChamps) :: champs
        !> Nombre de points explorateurs par carte de champ (défaut 0).
        !! Il est calculé lors de la vérification de la configuration et il est égal à :
        !! - n pour le segment, cercle et arc,
        !! - n12*n13 pour le rectangle,
        !! - n*nl pour le cylindre,
        !! - le nombre de lignes lues dans le fichier.
        !! Ceci n'est pas un réglage utilisateur.
        integer :: nb
        !> Nombre de points explorateurs du segment, cercle ou arc
        integer :: n
        !> Nom complet du fichier utilisé pour lire des points ou des éléments explorateurs.
        !! Ce fichier est tabulé en colonnes séparées par des espaces blancs et contenant, pour chaque ligne,
        !! soit un point en 3 colonnes pour décrire ses coordonnées cartésiennes,
        !! soit un élément en 1 colonne pour définir son indice de maillage
        character(len=200) :: fichier
        !> Premier point (coordonnées cartésiennes) pour définir le segment ou le rectangle
        real(kind(0d0)), dimension(3) :: point1
        !> Deuxième point (coordonnées cartésiennes) pour définir le segment ou le rectangle
        real(kind(0d0)), dimension(3) :: point2
        !> Troisième point (coordonnées cartésiennes) pour définir le rectangle
        real(kind(0d0)), dimension(3) :: point3
        !> Nombre de points entre point1 et point2 pour définir le rectangle
        integer :: n12
        !> Nombre de points entre point1 et point3 pour définir le rectangle
        integer :: n13
        !> Axe (coordonnées cartésiennes du vecteur) pour définir le cercle ou l'arc
        real(kind(0d0)), dimension(3) :: axe
        !> Centre (coordonnées cartésiennes) pour définir le cercle ou l'arc
        real(kind(0d0)), dimension(3) :: centre
        !> Angle (degrés, positif ou négatif) total de l'arc
        real(kind(0d0)) :: angle
        !> Longueur du cylindre (en m), positive ou négative
        real(kind(0d0)) :: longueur
        !> Nombre de points le long du cylindre
        integer :: nl
        !> Liste des points explorateurs sur lesquels chaque champ est sorti sous forme de carte (cf. carteChamps).
        !! Ce filtrage, i.e., la liste des points, n'est disponible que pour le format CSV (sélectionné automatiquement),
        !! car ce filtrage peut être réalisé par d'autres outils pour les autres formats.
        !! Ce tableau a deux dimensions : (liste des points, valeur de chaque point), e.g., coord( 3, (/0.0,1.0,2.0/) ).
        !! ATTENTION! La priorité est donnée à la liste des éléments explorateurs (elementsExplorateursChamp) si elle est définie avec un point explorateur.
        real(kind(0d0)), dimension(:,:), allocatable :: coord
        !> Liste des éléments explorateurs sur lesquels chaque champ est sorti sous forme de carte (cf. carteChamps).
        !! Ce filtrage, i.e., la liste des éléments, n'est disponible que pour le format CSV (sélectionné automatiquement),
        !! car ce filtrage peut être réalisé par d'autres outils pour les autres formats.
        !! Ce tableau a une dimension, la liste des indices du maillage des éléments voulus, e.g., elements(1) = 11080.
        !!  ou chaque élément est fourni par l'indice de son maillage.
        !! Ceci permet de tracer une ligne de coupe et d'utiliser les outils existants liés au tracé d'éléments.
        integer, dimension(:), allocatable :: elements
        !> Demande de suivi temporel des points explorateurs ou des éléments pour le filtrage des cartes de champ (défaut : non = .false.)
        logical :: evolution
        !> Liste des milieux sur laquelle la recherche est filtrée
        logical, dimension(1:kNombreMaximumMilieux) :: milieux
        !> Liste des indices internes des éléments correspondant aux milieux sur laquelle la recherche est filtrée
        !! Cette liste est optionnelle. Il faut au moins un milieu de sélectionné.
        !! Ceci n'est pas un réglage utilisateur.
        integer, dimension(:), allocatable :: ieltMilieux
        !> Angle (degrés, positif ou négatif) entre deux points de l'arc ou du cercle,
        !! utilisé pour écrire la valeur de l'angle du point dans le fichier de sortie
        !! Ceci n'est pas un réglage utilisateur.
        real(kind(0d0)) :: dangle
        !> Coordonnées des points explorateurs dans l'élément de référence.
        !! Ceci sera utilisé lors du post-traitement, par les routines d'interpolation, à l'instar des points de Gauss.
        !! Ce tableau a deux dimensions : (liste des points, coordonnées de chaque point), e.g., coordRef( 3, (/0.0,1.0,0.0/) ).
        !! Ceci n'est pas un réglage utilisateur.
        real(kind(0d0)), dimension(:,:), allocatable :: coordRef
        !> Correspondance, calculée au début du programme, entre chaque point explorateur (pointsExplorateursChamp)
        !!  ou chaque élément fourni par l'indice de son maillage (elementsExplorateursChamp) et l'indice interne de l'élément demandé
        !! Ceci permet de tracer une ligne de coupe et d'utiliser les outils existants liés au tracé d'éléments.
        !! Ce tableau a une dimension, la liste des indices internes des éléments voulus ou contenant le point voulu.
        !! Ceci n'est pas un réglage utilisateur.
        integer, dimension(:), allocatable :: ielt
        !> Suivi du calcul et de l'écriture des points explorateurs, à tout pas de temps.
        !! Les points explorateurs ont déjà été écrits pour ce pas de temps et les champs (.true.) ou non (.false.).
        !! Ceci est nécessaire car il existe deux moyens d'écrire les points explorateurs : interpoler lors de la simulation,
        !! ou réaliser une approximation à partir de la carte du champ, constante par élément, en cours de simulation ou a posteriori.
        !! Ceci n'est pas un réglage utilisateur.
        logical, dimension(1:kNombreMaximumChamps) :: ecrits
        !
       END TYPE pointsExplorateursChamps_type

       !> Taille du type donnees, en octets
       integer(kLong), parameter :: kTaillePointsExplorateursChamps = &
        250*kCharacter + kInteger + 16*kReal + 2*(1+kNombreMaximumChamps)*kLogical

       !> Informations relatives au maillage MED (version 3)
       TYPE infosMaillageMed
         !> Liste des noms de groupes (éléments et noeuds), classés par ordre lexicographique
         CHARACTER(LEN=80), DIMENSION(1:kNombreMaximumGroupes) :: listeNomGroupe
         !> Nombre réel de groupes
         INTEGER :: nbGroupes
         !> Nom du maillage (maa)
         CHARACTER(LEN=64) :: nomMaillage
         !> Nom du fichier texte fournissant la liste de tous les groupes
         CHARACTER(LEN=14) :: nomFichierGroupes = 'groupesMED.txt'
         !> Nombre de familles
         INTEGER :: nbFamilles
         !> Label de chaque famille (tableau dynamique)
         INTEGER, DIMENSION(:), ALLOCATABLE :: labelFamilles
         !> Nombre de groupes pour chaque famille (tableau dynamique)
         INTEGER, DIMENSION(:), ALLOCATABLE :: nbGroupesFamilles
       END TYPE

       !> Taille du type infosMaillageMed, en octets (statique)
       integer(kLong), parameter :: kTailleinfosMaillageMed = 2*kInteger +  (64+14+80*kNombreMaximumGroupes)*kCharacter

       !> Paramètre utilisé par la variable modeNomGroupeMed.
       !! Pas d'appariemment automatique des noms de groupes du maillage MED.
       !! Il faut indiquer les noms explicitement, et utiliser soit leur indice (cf. groupesMED.txt)
       !! soit utiliser la routine indiceGroupe pour récupérer leur indice à partir de leur nom.
       integer, parameter :: kNomGroupeMedExplicite = 1
       !> Paramètre utilisé par la variable modeNomGroupeMed.
       !! Appariemment automatique des noms de groupes du maillage MED, selon la convention de nommage MED2EDF,
       !! i.e., les noms de groupes de noeuds, e.g., pour les conditions aux limites des champs, commencent par n_,
       !! et les noms de groupes de volumes, e.g., pour les milieux, commencent par v_.
       !! L'affectation des noms de groupes est automatique, en supposant que les groupes de milieux commencent
       !!  au premier groupe de volume trouvé dans l'ordre lexicographique.
       integer, parameter :: kNomGroupeMed2EDF = 2

       ! +---------------------------------------------------------------------+
       ! | Variables publiques et privees.                                     |
       ! +---------------------------------------------------------------------+

       !> Instance du type infosMaillageMed
       TYPE(infosMaillageMed), save :: maillageMed

       !> Mode d'appariemment des noms de groupes du maillage MED 3 avec leur indice de position dans la liste des groupes
       !! Les valeurs possibles sont : kNomGroupeMedExplicite ou kNomGroupeMed2EDF
       integer :: modeNomGroupeMed

       !> Nombre de noeuds du maillage.
       INTEGER :: nombreNoeuds

       !> Liste des noeuds du maillage.
       TYPE(NOEUD), DIMENSION(:), ALLOCATABLE :: listeNoeuds

       !> Nombre d'aretes du maillage.
       INTEGER :: nombreAretes

       !> Liste des arêtes du maillage.
       TYPE(ARETE),DIMENSION(:), ALLOCATABLE :: listeAretes

       !> Nombre de facettes du maillage.
       INTEGER :: nombreFacettes
       !> Liste des facettes du maillage.
       TYPE(FACETTE), DIMENSION(:), ALLOCATABLE :: listeFacettes

       !> Nombre total d'éléments du maillage.
       INTEGER :: nombreElements
       !> Nombre d'éléments du maillage, par type d'élément (prisme, tétraèdre, etc).
       !! Cf. constantes kIndiceTetraedre, etc. pour l'association à chaque type.
       !! Ce nombre est affiché dans le programme principal.
       INTEGER, DIMENSION(1:kNombreElementsReference) :: nombreElementsParType

       !> Liste des elements du maillage.
       TYPE(ELEMENT), DIMENSION(:), ALLOCATABLE :: listeElements

       ! Section dédiée à l'assemblage des matrices et sources séparé linéaire et non-linéaire séparées
       ! afin d'accélérer cet assemblage lors de la résolution non-linéaire.
       !
       !> Assemblage séparé linéaire et non-linéaire (.true.) ou non (.false.)
       !! C'est un réglage utilisateur
       logical :: separerAssemblageLNL
       !> Nombre d'éléments non-linéaires
       integer :: nombreElementsNL
       !> Listes des éléments non-linéaires du maillage
       integer, dimension(:), allocatable :: listeElementsNL
       !> Nombre d'éléments linéaires
       integer :: nombreElementsL
       !> Listes des éléments linéaires du maillage
       integer, dimension(:), allocatable :: listeElementsL
       !> Sauvegarde de la partie linéaire du système
       TYPE(SYSTEME_MORSE) :: systemeL

       !> Nombre de groupes de noeuds présents dans le maillage.
       INTEGER :: nombreGroupesNoeuds
       !> Nombre de groupes d'éléments présents dans le maillage.
       integer :: nombreGroupesElements

       !> Nombre de milieux
       INTEGER :: nombreMilieux
       !> Liste des milieux, de taille maximale kNombreMaximumMilieux
       TYPE(MILIEU), DIMENSION(1:kNombreMaximumMilieux) :: listeMilieux

       !> Nombre de mouvements
       INTEGER :: nombreMouvements
       !> Liste des mouvements
       TYPE(MOUVEMENT), DIMENSION(1:knombreMaximumMouvement) :: listeMouvements
       !> Liste des elements en mouvements
       INTEGER :: nombreElementsMouvement
       TYPE(ELEMENTMOUVEMENT), DIMENSION(1:knombreMaximumElementMouvement) :: listeElementsMouvement
       !> Correction du problème de couture lors du mouvement libre, e.g., machine complète (.true.) ou non (.false.)
       logical :: coutureMvtLibre

       ! Liste des conditions de periodicite.
       INTEGER :: nombreConditionsPeriodicites
       TYPE(CONDITION_PERIODICITE), DIMENSION(1:knombreMaximumconditionPeridicite) :: &
                                                    listeConditionsPeriodicites

       ! Liste des conditions limites sur les champs
       INTEGER :: nombreConditionsLimitesChamps
       TYPE(CONDITIONSLIMITESCHAMPS), DIMENSION(1:knombreMaximumconditionChamp) :: &
                                                    listeConditionsLimitesChamps

       ! Liste des types de conditions a la limite.
       INTEGER :: nombreConditionsLimites
       TYPE(CONDITION_LIMITE), DIMENSION(1:kNombreMaximumConditionsLimites) :: &
                                                          listeConditionsLimites
       !> Paramètre permettant d'activer (.true.) le regroupement des faces de conditions aux limites entourant un même milieu, ou pas (.false.).
       !! Julien Korecki, pour les besoins du projet CAMELIA, a rajouté une fonctionnalité,
       !! permettant de n'utiliser qu'un seul groupe de noeuds pour définir plusieurs faces d'un même milieu,
       !! faces soumises à la même condition aux limites.
       !! Bien que la programmation soit générale, il ne l'a validé que pour les formulations T ou T-Omega (cela s'applique aux arêtes) et la CL Jind.n=0.
       !!
       !! Cette modification, appliquée en dur dans le code, permet de toujours considérer comme inconnues des arêtes internes,
       !! i.e., n'appartenant pas à ces faces mais dont les 2 noeuds appartiennent à ces faces.
       !! Sans cette modification, ces arêtes sont considérées comme des conditions aux limites.
       !
       logical :: clJindn0Tmonogroupe

       ! Liste des macro-groupes de noeuds.
       INTEGER :: nombreMGroupesNoeuds
       TYPE(MGROUPE_NOEUDS), DIMENSION(1:kNombreMaximumMGroupesNoeuds) :: &
                                                             listeMGroupesNoeuds

       ! Liste des macro-groupes d'aretes.
       INTEGER :: nombreMGroupesAretes
       TYPE(MGROUPE_ARETES), DIMENSION(1:kNombreMaximumMGroupesAretes) :: &
                                                             listeMGroupesAretes

       ! Liste des macro-groupes de facettes.
       INTEGER :: nombreMGroupesFacettes
       TYPE(MGROUPE_FACETTES), DIMENSION(1:kNombreMaximumMGroupesFacettes) :: &
                                                           listeMGroupesFacettes

       ! Liste des macro-groupes de milieux.
       INTEGER :: nombreMGroupesMilieux
       TYPE(MGROUPE_MILIEUX), DIMENSION(1:kNombreMaximumMGroupesMilieux) :: &
                                                            listeMGroupesMilieux

       ! Liste des inducteurs.
       INTEGER :: nombreInducteurs
       TYPE(INDUCTEUR), DIMENSION(1:kNombreMaximumInducteurs) :: listeInducteurs

       ! Liste des inducteurs.
       INTEGER :: nombreTermeSourceMagnetique
       TYPE(TERMESOURCEMAGN), DIMENSION(1:kNombreMaximumSourceMagn) :: listeTermesSourcesMagn

       INTEGER :: nombreAimants
       TYPE(AIMANT_TYPE), DIMENSION(1:kNombreMaximumAimants) :: listeAimants

       ! Liste des spires exploratrices.
       INTEGER :: nombreSpireExploratrice, numeroSpireExploratrice
       TYPE(SpireExploratrice), DIMENSION(1:kNombreMaximumSpireExploratrice) ::&
                                                        listeSpireExploratrice

       ! Liste des groupes d elements donnant des flux.
       INTEGER :: nombreGroupeElementFlux
       TYPE(GroupeElementFlux), DIMENSION(1:kNombreMaximumGroupeElementFlux) ::&
                                                        listeGroupeElementFlux

       !> Nombre de forceCouple.
       INTEGER :: nombreForceCouple
       !> Liste des forceCouple.
       TYPE(ForceCouple), DIMENSION(1:kNombreMaximumForceCouple) :: listeForceCouple

       ! Liste des coupures.
       INTEGER :: nombreCoupure
       TYPE(coupure), DIMENSION(1:kNombreMaximumCoupure) :: listeCoupure

       ! Liste de potentiel flottant.
       INTEGER :: nombrePotentielFlottant
       TYPE(POTENTIELFLOTTANT), DIMENSION(1:knombreMaximumPotFlottant) :: &
                                                    listePotentielFlottant


       ! Points de Gauss.
       TYPE(POINTS_GAUSS), TARGET :: pointsGaussTriangleRef
       TYPE(POINTS_GAUSS), TARGET :: pointsGaussRectangleRef
       TYPE(POINTS_GAUSS) :: pointsGaussTetraedreRef
       TYPE(POINTS_GAUSS) :: pointsGaussPrismeRef
       TYPE(POINTS_GAUSS) :: pointsGaussHexaedreRef

       ! Type de probleme.
       ! 1 : magnetostatique (= kMagnetostatique).
       ! 2 : electrocinetique (= kElectrocinetique).
       ! 3 : magnetodynamique (= kMagnetodynamique).
       INTEGER typeProbleme

       ! Nature de terme à determiner et assembler  groupe par groupe
       TYPE(NATURE_ASSEMBLER), DIMENSION(knombreprobleme)  :: probleme
       ! Sous Systeme d'equations.
       TYPE(SOUS_SYSTEME_MORSE), DIMENSION(:), ALLOCATABLE :: sous_systeme
       TYPE(SOUS_SYSTEME_MORSE), DIMENSION(:), ALLOCATABLE :: RotnuRot
       TYPE(SOUS_SYSTEME_MORSE), DIMENSION(:), ALLOCATABLE :: RotdnuRot

       ! Systeme d'equations.
       TYPE(SYSTEME_MORSE) :: systeme

       !> Indice de la boucle du temps.
       INTEGER :: indBoucleTemps
       !> Indice de la boucle non-linéaire
       INTEGER :: indBoucleNonLineaire

       !> Nombre de pas de temps;
       INTEGER :: NBoucleTemps

       !> Pas de temps.
       REAL(KIND(0D0)) :: dt
       !> Valeur du temps courant
       REAL(KIND(0D0)) :: t
       !> Liste contenant toutes les valeurs des pas de temps permis
       real(kind(0d0)), dimension(:), allocatable :: listeDt
       !> Liste contenant toutes les valeurs des temps permis
       real(kind(0d0)), dimension(:), allocatable :: listeTemps

       ! Méthodes de résolution non-linéaires
       !
       !> Choix de la méthode par l'utilisateur
       integer :: methodeNonLineaire
       !> Méthode de la substitution
       integer, parameter :: kMethodeSubstitution = 1
       !> Méthode de Newton-Raphson
       integer, parameter :: kMethodeNewton = 2
       !> Précision non-linéaire demandée
       REAL(KIND(0D0)) :: kEpsilonNonLinearite
       !> Relaxation de la solution si compris entre 0 et 1.
       REAL(KIND(0D0)) :: kCoefficientRelaxation
       !> Remise à zéro de la solution initiale non-linéaire (.true.) ou non (.false.).
       !! Ce dernier choix permet d'accélérer la convergence non-linéaire dans la plupart des cas,
       !! avec la méthode de Newton-Raphson seulement
       logical :: Raznewton
       !> Nombre maximum d'itérations non-linéaires
       integer :: nbIterationMaxNL

       ! Definition des fichiers de sortie
       TYPE(donnees) :: postraitementdonne
       ! Definition des groupes de points explorateurs
       TYPE(pointsExplorateursChamps_type), dimension(1:kNombreMaximumGroupesPointsExplorateursChamp) :: pointsExplorateursChamps

       !> Choix de la formulation par l'utilisateur (non défini par défaut),
       !! 1 en HsOmega et 2 en A
       INTEGER :: formulation

       ! Parametre de la teta methode
       REAL(KIND(0D0)) :: f

       !jauge en A ou en T.
       LOGICAL :: jauge

       !> Descripteur de fichier utilisé à divers endroits du code.
       !! Cela permet de ne pas avoir à définir un entier dans les routines
       !! lorsque on ouvre un seul fichier à la fois.
       INTEGER :: descrip
       !> Descripteur de fichier utilisé pour le format VTK
       INTEGER :: descripteurVTK


       !> Nom de fichier pour écrire ou lire dans les fichiers du code.
       !! Cela permet de ne pas avoir à définir une chaîne dans les routines
       !! lorsque on ouvre un seul fichier à la fois.
       !! Chaîne de caractère courte (200 caractères)
       character(len=200) :: fichier
       ! Variable de controle de N et K.
       REAL(KIND(0D0)) :: indiceDivN
       REAL(KIND(0D0)) :: indiceRotK
       REAL(KIND(0D0)) :: circulationMaxK
       INTEGER, DIMENSION(1:2) :: IndiceAreteIndMaxK

       ! nombre de permutations pas pas temporel.
       INTEGER :: nbPermutPas, NbIter

       ! Constantes mathématiques ou physiques
       !> pi de grande précision (21 chiffres significatifs, i.e., mieux que les 16 chiffres de la double précision par défaut)
       REAL(KIND(0D0)), PARAMETER :: kPI = 3.14159265358979323846d0
       !> perméabilité du vide mu_0 = 4e-7*pi
       REAL(KIND(0D0)), PARAMETER :: kMU0 = 4.0d-7*kPI
       !> réluctivité du vide nu_0 = 1/mu_0
       REAL(KIND(0D0)), PARAMETER :: kNU0 = 1.0d0/kMU0
       ! Constantes mathématiques utilisées dans les versions antérieures du code, pour comparer finement.
       !> pi (10 chiffres significatifs),
       !! routine bio_savart_rect (CalculFlux.f90), versions 1.6.2 à ?
       REAL(KIND(0D0)), PARAMETER :: kPI10 = kPI !3.141592654d0
       !> pi (10 chiffres significatifs, arrondi inférieur),
       !! routine ConditionsPeriodicites (periodiciteMouvement.f90), versions 1.6.2. à ?
       REAL(KIND(0D0)), PARAMETER :: kPI10i = kPI !3.141592653d0
       !> pi (6 chiffres significatifs), utilisé dans les versions 1.6.2. à ?,
       !! dans les routines : ConditionsPeriodicites, IndiceNoeudApermuter et IndiceAreteApermuter (periodiciteMouvement.f90).
       REAL(KIND(0D0)), PARAMETER :: kPI6 = kPI !3.14159d0
       !> perméabilité du vide mu_0 = 4e-7*pi où pi a 6 chiffres significatifs,
       !! utilisé dans les versions 1.6.2. à 1.12.1, pour définir la perméabilité du vide dans les routines :
       !! - definirCaracteristiquesMilieux (configurationInitiale.f90 et configuration.f90),
       !! - permeabiliteMagnetiqueB (utilitaires.f90 jusqu'à la version 1.10.0, loisComportement.f90 à partir de la version 1.10.1)
       REAL(KIND(0D0)), PARAMETER :: kMU06 = kMU0 !4.0d-7*kPI6
       !> réluctivité du vide nu_0 = 1/mu_0
       !! utilisé dans les versions 1.6.2. à 1.12.2,
       !! pour définir la réluctivité du vide pour le calcul de la force par la méthode des travaux virtuels : routine melcpl (forceCoupleTV.f90).
       !! Bien que le nombre de chiffres soit de 8 ici, cette valeur correspond à peu près à un nombre Pi avec 10 chiffres significatifs,
       !!  mais pas tout à fait sur le dernier chiffre.
       REAL(KIND(0D0)), PARAMETER :: kNU010i = kNU0 !795.77471d+03

       ! Affichage de la convergence du solveur linéaire sous forme de barre
       !
       !> Affiche une barre horizontale pour la convergence (.true.) ou une liste (.false.)
       logical :: afficheBarreConvergence
       !> taille de la barre (en largeurs de caractères =)
       integer, parameter :: kTailleBarre = 25
       !> caractère pour le retour de barre
       character(len=1), parameter :: kBarreRetour = char(8)
       !> caractère de la barre (=)
       character(len=1), parameter :: kBarre = '='

       !
       ! Sauvegarde de la convergence dans un fichier
       !

       !> Nom du fichier dans lequel la convergence du solveur linéaire itératif gradient conjugué (GC) est sauvegardée
       !! Si ce nom n'est pas vide, la convergence n'est pas sauvegardée
       character(len=80) :: sauveConvergenceGC

       !> Nom du fichier dans lequel la convergence du solveur non-linéaire est sauvegardée.
       !! Si ce nom n'est pas vide, la convergence n'est pas sauvegardée
       character(len=80) :: sauveConvergenceNL

       !> indicateur de problème non-linéaire (.true.) ou linéaire (.false.)
       logical :: problemeNonLineaire

       ! Choix automatique d'un numéro d'unité pour l'ouverture d'un fichier et l'écriture (cf. utilitaires)
       !> Les numéros libres vont d'un minimum à un maximum
       integer, parameter :: ionum_min=10,ionum_max=99
       logical, dimension(ionum_min:ionum_max) :: ionum_available
       logical :: ionum_initialized=.false.

       !> gestion des erreurs, indices global
       integer :: erreur
       !> Indique que le programme est arrêté (stoppé) en cas d'erreur (.true.) ou non (.false.)
       logical :: stopSiErreur

       !> Certains messages affichés à l'écran sont colorés (.true.), selon leur contexte ou non (.false.).
       !! C'est un réglage utilisateur, afin de désactiver ces couleurs si l'affichage est redirigé vers un fichier.
       !! En ce cas, les couleurs redeviennent des codes difficilement lisibles.
       logical :: couleurMessages

       !> Calcul de N une seule fois par éxécution du programme, même si on enchaîne les formulations
       logical, save :: calculN = .true.
       !> Calcul de K une seule fois par éxécution du programme, même si on enchaîne les formulations, et seulement pour la formulation HsOmega
       logical, save :: calculK = .true.

        !> Ce type contient les résultats de comparaison entre 2 grandeurs réelles, de type scalaire, table 1D, 2D ou 3D.
        !! Ceci sert à comparer les résultats de post-traitement avec une référence, e.g., pour les auto-tests.
        type TYPE_COMPARAISON
           !> Valeur maximale de la différence absolue entre les 2 grandeurs
           real(kind(0d0)) :: abs_l2err_max
           !> Valeur moyenne, au sens R.M.S, de la différence absolue entre les 2 grandeurs
           real(kind(0d0)) :: abs_l2err_rms
           !> Valeur minimale de la différence absolue entre les 2 grandeurs
           real(kind(0d0)) :: abs_l2err_min
           !> Valeur maximale de la différence relative entre les 2 grandeurs
           real(kind(0d0)) :: rel_l2err_max
           !> Valeur moyenne, au sens R.M.S, de la différence relative entre les 2 grandeurs
           real(kind(0d0)) :: rel_l2err_rms
           !> Valeur minimale de la différence relative entre les 2 grandeurs
           real(kind(0d0)) :: rel_l2err_min
           !> Valeur maximale de la norme de la grandeur de référence
           real(kind(0d0)) :: l2nrm_max
           !> Valeur moyenne, au sens R.M.S., de la norme de la grandeur de référence
           real(kind(0d0)) :: l2nrm_rms
           !> Valeur minimale de la norme de la grandeur de référence
           real(kind(0d0)) :: l2nrm_min
           !> Format de la référence, qui prend les valeurs (paramètres) ci-dessous :
           !! - kFormatActuel : format actuel, i.e., le formatage des pas de temps sur 4 chiffres.
           !! - kFormatCarmel16 : format code_Carmel 1.6, i.e., le formatage des pas de temps sur 3 chiffres.
           integer :: formatReference
           !> Cette comparaison a été initialisée ou pas encore modifiée avec des valeurs réelles de comparaison (.true.) ou non (.false.)
           !! Cela permet de mettre à jour la comparaison globale la première fois que l'on sauvegarde une comparaison.
           !! C'est une variable à usage interne
           logical :: init
           !> Affichage de la comparaison dans le terminal (.true.) ou non (.false.).
           !! Ceci est défini par l'utilisateur (configuration) pour être utilisé dans le reste du code lors des comparaisons
           logical :: afficher
           !> Sauvegarde de la comparaison dans un fichier (.true.) ou non (.false.).
           !! Ceci est défini par l'utilisateur (configuration) pour être utilisé dans le reste du code lors des comparaisons
           logical :: sauvegarder
           !> Nom du dossier contenant la solution de référence
           character(len=80) :: dossierReference
           !> Permet de filtrer les valeurs lues au dessus de ce rapport multiplié par la valeur maximale du champ (par composante) ou de la table :
           !! - 2 pour garder toutes les valeurs mais appliquer le calcul R.M.S. pour l'erreur relative maximale
           !! - 1 pour ne garder que la valeur maximale,
           !! - valeur strictement comprise entre 0 et 1 pour ne garder qu'une partie des valeurs, e.g., 0.1 pour ne garder que 90% des valeurs les plus grandes,
           !!    au sens où l'on garde les valeurs supérieures à 0.1 * magnitude de la valeur la plus grande.
           !! - 0 pour ne supprimer que les valeurs conjointement nulles,
           !! - -1 pour comparer toutes les valeurs.
           real(kind(0d0)) :: filtrage
           !
        end type TYPE_COMPARAISON

        !> Taille du type TYPE_COMPARAISON, en octets
        integer(kLong), parameter :: kTailleTYPE_COMPARAISON = 9*kReal + kInteger + 3*kLogical + 80*kCharacter

        !> Format Actuel des sorties de post-traitement, i.e., le formatage des pas de temps sur 4 chiffres.
        integer, parameter :: kFormatActuel = 1
        !> Format code_Carmel 1.6 des sorties de post-traitement, i.e., le formatage des pas de temps sur 3 chiffres.
        integer, parameter :: kFormatCarmel16 = 2

        !> Variable globale pour la comparaison entre résultats de post-traitement et une référence
        type(TYPE_COMPARAISON) :: comparaison

        !> Drapeau pour activer (.true.) ou pas (.false.) des messages de déboguage
        logical :: debug
        !> Mode de vérification de la configuration.
        !! Cette vérification pouvant être un peu longue, elle peut être désactivée ou réduite.
        !! Les choix possibles sont :
        !! - 'complete' (par défaut) : vérification de la simulation complète avant démarrage de la simulation,
        !! - 'encours' : vérification de la configuration ne dépendant pas du temps, puis vérification lors de la simulation au pas de temps en cours,
        !! - 'minimal' : vérification de la configuration ne dépendant pas du temps seulement,
        !! - 'non' : aucune vérification.
        character(len=8) :: checkConfiguration

        !> Longue chaîne de caractère pour enregistrer un message, e.g., d'erreur
        character(len=1000) :: message

        !> Pour conversion d'octets en Mo
        INTEGER, PARAMETER :: mega=1024*1024
        !> Plus petit et plus grand reel en simple precision
        REAL(KIND(0D0)), PARAMETER :: real_sp_min=1.0D-37,  real_sp_max=1.0D+37
        !> Plus petit et plus grand reel en double precision
        REAL(KIND(0D0)), PARAMETER :: real_dp_min=1.0D-307, real_dp_max=1.0D+307
        !> Précision de la machine, en double précision
        REAL(KIND(0D0)), PARAMETER :: epsilon_machine=1.d-14

        !> Calcul de la taille en mémoire (octets) des données statiques
        integer(kLong), parameter :: kTailleStatique = &
            kNombreElementsReference*kTailleELEMENT_REFERENCE &
            + kTailleinfosMaillageMed &
            + knombreMaximumElementMouvement*kTailleELEMENTMOUVEMENT &
            + knombreMaximumconditionPeridicite*kTailleCONDITION_PERIODICITE &
            + knombreMaximumMouvement*kTailleMOUVEMENT &
            + knombreMaximumconditionChamp*kTailleCONDITIONSLIMITESCHAMPS &
            + kNombreMaximumConditionsLimites*kTailleCONDITION_LIMITE &
            + kNombreMaximumMGroupesNoeuds*kTailleMGROUPE_NOEUDS &
            + kNombreMaximumMGroupesAretes*kTailleMGROUPE_ARETES &
            + kNombreMaximumMGroupesFacettes*kTailleMGROUPE_FACETTES &
            + kNombreMaximumMGroupesMilieux*kTailleMGROUPE_MILIEUX &
            + kNombreMaximumInducteurs*kTailleINDUCTEUR &
            + kNombreMaximumSourceMagn*kTailleTERMESOURCEMAGN &
            + kNombreMaximumAimants*kTailleAIMANT &
            + kNombreMaximumSpireExploratrice*kTailleSpireExploratrice &
            + kNombreMaximumGroupeElementFlux*kTailleGroupeElementFlux &
            + kNombreMaximumForceCouple*kTailleForceCouple &
            + knombreMaximumPotFlottant*kTaillePOTENTIELFLOTTANT &
            + 6*kTaillePOINTS_GAUSS &
            + knombreprobleme*kTailleNATURE_ASSEMBLER &
            + kTailleSYSTEME_MORSE &
            + kTailledonnees &
            + (ionum_max-ionum_min+1)*kLogical &
            + kTailleTYPE_COMPARAISON &
            + kNombreMaximumGroupesPointsExplorateursChamp*kTaillePointsExplorateursChamps

      ! Pour le couplage circuit avec qucs T1.11.X, T1.12.X
      integer :: nombreresistancecirc,nombresourcetensioncirc,nombreinducteurefcirc,nombrearbrecirc
      integer :: nombredipole
      integer :: nombrebouclecourant
      integer,dimension(kNombreMaximumDipole,kNombreMaximumArbreCirc)     :: matJI
      integer,dimension(kNombreMaximumArbreCirc,kNombreMaximumDipole)     :: matJIT

      ! ajout d'un type dipole faisant le lien entre les inducteurs et les dipoles
      type dipole_type
             character(len=30) :: nomqucs
             integer :: netin,netout, indiceinducteur
             real(kind(0.0d0)) :: valeur,frequence,phase   ! lu dans qucs
             real(kind(0.0D0)) :: courant,tension          ! calculer dans le code
             real(kind(0.0D0)) :: courant0,tension0          ! calculer dans le code
             character(len=5) :: nature  ! R, Vac, Vdc, Ind, Vfile
             !> Nom du fichier contenant la liste des tensions à lire pour le dipôle Vfile,
             !! sous la forme de chemin absolu depuis le fichier Netlist
             !! et sous la forme relative pour exploitation dans le dossier courant de la simulation
             character(len=200) :: chemin, fichier
      end type

      ! ajout d'un type maille relatif aux courants de mailles
      type maille_type
             integer                                   :: inconnue
             integer,dimension(kNombreMaximumDipole)   :: listearetes
             integer                                   :: nombrearetes
             integer,dimension(kNombreMaximumDipole)   :: signe
             real(kind(0.0D0)) :: courant
      end type

      Real(kind(0.0D0)),dimension(kNombreMaximumDipole) :: vectV,vectUc0,vectUC,vectI,vectI0,vectUR,vectUL ! vecteur des tensions sources pour le couplage circuit
      Real(kind(0.0D0)),dimension(kNombreMaximumDipole,kNombreMaximumDipole) :: matZ,matR,matL,matC ! Matrice des resistances

      type(dipole_type),dimension(kNombreMaximumDipole) :: listedipole
      type(maille_type),dimension(kNombreMaximumArbreCirc) :: listemailles

      logical :: lectureQucs ! variable permettant de définir si un fichier netliste circuit est à lire ou non

      !
      ! Lecture UNV Salome et sortie maillage VTK
      ! a partir du programme unvSalome2Ideas de Jean-Pierre Ducreux (2014/01/20)
      !
      !> Type contenant des infos sur un groupe de noeuds lu dans le maillage UNV au format Salome
      !! Ce type permet de stocker les informations lues pour les utiliser lors de l'écriture du maillage au format I-Deas éventuellement
      TYPE GROUPE
            INTEGER      :: numeroGroupeInt
            CHARACTER(1) :: numeroGroupeChar_1
            CHARACTER(2) :: numeroGroupeChar_2
            CHARACTER(3) :: numeroGroupeChar_3
            CHARACTER(4) :: numeroGroupeChar_4
            INTEGER      :: nombreNoeudsGroupe
            INTEGER      :: nbLignesPleines
            INTEGER      :: residu
            INTEGER, DIMENSION(:), allocatable :: listeIndicesNoeudsGroupe
      END TYPE GROUPE
      !> Liste des groupes de noeuds
      TYPE(GROUPE), DIMENSION(1:kNombreMaximumGroupes) :: listeGroupes
      !> sortie du maillage au format VTK (.true.) ou non (.false.), en cas de lecture de maillage UNV Salome
      LOGICAL :: outputVTK
      !> sortie du maillage au format UNV I-Deas (.true.) ou non (.false.), en cas de lecture de maillage UNV Salome
      LOGICAL :: outputUNVIDEAS
      !> Unité de mesure à enregistrer dans le maillage UNV I-Deas.
      !! Comme le maillage est transformé en m par défaut (S.I.), le choix par défaut est 1.
      !! Les choix possibles sont :
      !! -  1 - SI : Meter (newton)
      !! -  2 - BG : Foot (pound f)
      !! -  3 - MG : Meter (kilogram f)
      !! -  4 - BA : Foot (poundal)
      !! -  5 - MM : mm (milli newton)
      !! -  6 - CM : cm (centi newton)
      !! -  7 - IN : Inch (pound f)
      !! -  8 - GM : mm (kilogram f)
      !! -  9 - US : user defined
      !! - 10 - MN : mm (newton)
      !
      INTEGER :: choix_unite_mesure

      !> Sens de parcours de l'arbre d'arêtes du maillage pour calculer K sur les arêtes (cf. routine calculerArbreAretes, module source.f90) :
      !! - 'direct' : parcours de la première à la dernière arête (sens par défaut),
      !! - 'inverse' : parcours inverse de la dernière à la première arête.
      !! Il est connu que ce choix peut influencer la qualité de K.
      !! C'est un paramètre de configuration utilisateur.
      !
      character(len=10) :: sensArbreK

      !> Sens de parcours de l'arbre de facettes du maillage pour calculer N sur les facettes (cf. routine calculerArbreFacettes, module source.f90) :
      !! - 'direct' : parcours de la première à la dernière facette,
      !! - 'inverse' : parcours inverse de la dernière à la première facette (sens par défaut).
      !! Il est connu que ce choix peut influencer la qualité de N.
      !! C'est un paramètre de configuration utilisateur.
      !
      character(len=10) :: sensArbreN

      !
      ! Données internes pour l'inducteur enrichi avec une ligne directrice ou la minimisation (Antoine Pierquin, L2EP)
      !
      TYPE(SYSTEME_MORSE) :: matDiv, matArbre, matCoarbre, matInvCoarbre, matMinArbre, matMinCoarbre

      !> Au moins un inducteur est enrichi avec une ligne directrice ou la minimisation,
      !! valeur initialisée lors de la vérification de la configuration.
      logical :: inducteursCalculerJmin

     END MODULE
