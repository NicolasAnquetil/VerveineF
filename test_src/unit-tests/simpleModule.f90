MODULE simpleModule
  CHARACTER(LEN = 10), PARAMETER :: aString
  INTEGER, intent(inout) :: anInt = 19
  LOGICAL, PARAMETER, PUBLIC :: aBool = .false.
  REAL(KIND(0D0))  :: aReal = 2000.0D0
  integer, dimension(dim) :: dimVar =&
       (/init1, init2, init3, init4/)
END MODULE simpleModule
