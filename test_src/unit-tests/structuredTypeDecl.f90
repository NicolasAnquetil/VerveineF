MODULE simpleModule
  type structuredType
    CHARACTER(LEN = 10) :: aString
    INTEGER :: anInt = 19
    LOGICAL :: aBool = .false.
    REAL(KIND(0D0))  :: aReal = 2000.0D0
  end type
contains
    subroutine creerInstance(fft)
	type(typeFFT), intent(inout) :: fft
	fft%anInt = 0
    end subroutine creerInstance
END MODULE
