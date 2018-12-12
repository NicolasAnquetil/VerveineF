MODULE simpleModule
implicit none
  private :: aProc
  interface anotherProc
    module procedure aProc
  end interface

  integer :: aVar
END MODULE simpleModule
