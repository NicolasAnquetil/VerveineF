!> Module de prise en charge du format VTK (LEGACY VTK) pour le post-traitement.
!!
!! @author Jean-Pierre DUCREUX (EDF/THEMIS)
!! @author Loïc CHEVALLIER (L2EP)
!
module vtk

use structureDonnees
use utilitaires

implicit none

! +---------------------------------------------------------------------+
! | Constantes publiques et privees.                                    |
! +---------------------------------------------------------------------+

! Taille du buffer interne.
INTEGER, PARAMETER, PRIVATE :: kTailleChaine = 256

! +---------------------------------------------------------------------+
! | variable publiques et privees.                                      |
! +---------------------------------------------------------------------+



! +---------------------------------------------------------------------+
! | Fonctionnalites publiques et privees                                |
! +---------------------------------------------------------------------+

PUBLIC :: creerGrilleVTK
PUBLIC :: ecrireChampVecteurVTK
PUBLIC :: ecrireChampScalaireVTK


CONTAINS


       ! +---------------------------------------------------------------------+
       ! | PROCEDURE  : creerGrilleVTK                                  |
       ! | OBJET      : Initialisation de la liste des noeuds et des elements  |
       ! |            : du de la grille VTK.                                           |
       ! | PARAMETRES : - descripteur (mode IN):                               |
       ! |            :     handle fichier donne par l'utilisateur.            |            |
       ! |            : - erreur (mode INOUT) :                                |
       ! |            :     code de l'erreur rencontree.                       |
       ! |            : - ligne (mode OUT) :                                   |
       ! |            :     numero de la ligne ou l'erreur s'est produite.     |
       ! | REMARQUES  :  |
       ! |            :                              |
       ! +---------------------------------------------------------------------+

       SUBROUTINE creerGrilleVTK(descripteur)
            INTEGER, INTENT(IN)    :: descripteur

            ! Compteur de boucle
            INTEGER :: i
            !
            ! Entier pour le calcul du nombre total de noueds intervenant dans la description des éléments
            INTEGER :: size_listVTK
            !
            ! Informations relatives a l'element courant.
            TYPE(INFOS_ELEMENT) :: infosElement


            ! Ecriture de l'entete du fichier resultat au format VTK
            WRITE(descripteur, "(A)") "# vtk DataFile Version 4.0"
            WRITE(descripteur, "(A,A)") "Postpro - Ce fichier a ete genere par code_Carmel version ",trim(adjustl(kVersion))
            WRITE(descripteur, "(A)") "ASCII"
            WRITE(descripteur, "(A)") "DATASET UNSTRUCTURED_GRID"
            WRITE(descripteur, "(A,I10,A)") "POINTS ", nombreNoeuds, " double"

            ! Boucle sur les noeuds.
            DO i = 1, nombreNoeuds
               WRITE(descripteur, "(3E13.5)") listeNoeuds(i)%x,&
                                              listeNoeuds(i)%y,&
                                              listeNoeuds(i)%z
            END DO

            ! Calcul du nombre de noeuds total pour décrire les éléments
            size_listVTK = 0

            DO i =  1,nombreElements
               CALL informationsElement(i, infosElement)
               ! Obtention du nombre d'entiers pour decrire cet element = nombre de noeuds
               SELECT CASE(infosElement%iref)
                 CASE(kIndiceTetraedre)
                   size_listVTK = size_listVTK + 5

                 CASE(kIndicePrisme)
                   size_listVTK = size_listVTK + 7

                 CASE(kIndiceHexaedre)
                   size_listVTK = size_listVTK + 9

                 CASE DEFAULT
                   call stopErreur("Mauvais element dans le calcul de liste VTK",'creerGrilleVTK')
               END SELECT
            END DO
            !
            WRITE(descripteur, "(A,I10,A,I10)") "CELLS", nombreElements, " " , size_listVTK
            !Ecriture de la connectivite des cellules
            ! Boucle sur les elements pour connaitre leurs types et leurs noeuds
            DO i = 1, nombreElements
               CALL informationsElement(i, infosElement)
               ! Ecriture en fonction du type d'element
               SELECT CASE(infosElement%iref)
                 CASE(kIndiceTetraedre)
                   WRITE(descripteur, "(A,4I10)") "4",&
                                listeElements(i)%listeIndicesNoeuds(1)-1,&
                                listeElements(i)%listeIndicesNoeuds(2)-1,&
                                listeElements(i)%listeIndicesNoeuds(3)-1,&
                                listeElements(i)%listeIndicesNoeuds(4)-1

                 CASE(kIndicePrisme)
                   WRITE(descripteur, "(A,6I10)") "6",&
                                listeElements(i)%listeIndicesNoeuds(1)-1,&
                                listeElements(i)%listeIndicesNoeuds(2)-1,&
                                listeElements(i)%listeIndicesNoeuds(3)-1,&
                                listeElements(i)%listeIndicesNoeuds(4)-1,&
                                listeElements(i)%listeIndicesNoeuds(5)-1,&
                                listeElements(i)%listeIndicesNoeuds(6)-1

                 CASE(kIndiceHexaedre)
                   WRITE(descripteur, "(A,8I10)") "8",&
                                listeElements(i)%listeIndicesNoeuds(1)-1,&
                                listeElements(i)%listeIndicesNoeuds(2)-1,&
                                listeElements(i)%listeIndicesNoeuds(3)-1,&
                                listeElements(i)%listeIndicesNoeuds(4)-1,&
                                listeElements(i)%listeIndicesNoeuds(5)-1,&
                                listeElements(i)%listeIndicesNoeuds(6)-1,&
                                listeElements(i)%listeIndicesNoeuds(7)-1,&
                                listeElements(i)%listeIndicesNoeuds(8)-1

                 CASE DEFAULT
                   call stopErreur("Mauvais element dans le calcul de liste VTK",'creerGrilleVTK')
               END SELECT
            END DO
            !
            WRITE(descripteur, "(A,I10)") "CELL_TYPES", nombreElements
            !
            ! Boucle pour ecrire les types de cellules
            DO i = 1, nombreElements
               CALL informationsElement(i, infosElement)
               ! Obtention du type d'element pour décrire cet element
               SELECT CASE(infosElement%iref)
                 CASE(kIndiceTetraedre)
                   WRITE(descripteur, "(A)") "10"

                 CASE(kIndicePrisme)
                   WRITE(descripteurVTK, "(A)") "13"

                 CASE(kIndiceHexaedre)
                   WRITE(descripteurVTK, "(A)") "12"

                 CASE DEFAULT
                   call stopErreur("Mauvais element dans le calcul de liste VTK",'creerGrilleVTK')
               END SELECT
            END DO
            WRITE(descripteur, "(A,I10)") "CELL_DATA ", nombreElements
            WRITE(descripteur, "(A)") "SCALARS Materiau double 1"
            WRITE(descripteur, "(A)") "LOOKUP_TABLE default"
            DO i = 1, nombreElements
               WRITE(descripteur, *) listeElements(i)%indiceMilieu
            END DO
       END SUBROUTINE creerGrilleVTK
       !
       !
       SUBROUTINE ecrireChampVecteurVTK(typeChamp, champ, nom)
            ! arguments
            !> Type de champ (défini par des constantes, cf. structureDonnees).
            INTEGER, INTENT(IN) :: typeChamp
            !> Champ vectoriel en entrée à 2 dimensions (éléments ou noeuds, 3 composantes X, Y, Z)
            REAL(KIND(0D0)), DIMENSION(:,:), INTENT(IN) :: champ
            !> Nom du champ
            CHARACTER(LEN=*), INTENT(IN) :: nom
            !
            CHARACTER (LEN=80) :: buffer
            INTEGER :: i
            !
            !
            buffer = 'VECTORS '//trim(adjustl(nom))//' double'
            WRITE(descripteurVTK, "(A)") buffer
            DO i=1, nombreElements
               WRITE(descripteurVTK,"(3E13.5)") champ(i,1:3)
            END DO
       END SUBROUTINE ecrireChampVecteurVTK
       !
       !
       SUBROUTINE ecrireChampScalaireVTK(typeChamp, champ, nom)
            ! arguments
            !> Type de champ (défini par des constantes, cf. structureDonnees).
            INTEGER, INTENT(IN) :: typeChamp
            !> Champ scalaire en entrée (éléments ou noeuds)
            REAL(KIND(0D0)), DIMENSION(:), INTENT(IN) :: champ
            !> Nom du champ
            CHARACTER(LEN=*), INTENT(IN) :: nom
            !
            CHARACTER (LEN=80) :: buffer
            INTEGER :: i
            !
            !
            buffer = 'SCALARS '//trim(adjustl(nom))//' double 1'
            WRITE(descripteurVTK, "(A)") buffer
            DO i=1, nombreElements
               WRITE(descripteurVTK,"(E13.5)") champ(i)
            END DO
       END SUBROUTINE ecrireChampScalaireVTK
end module vtk
