MODULE simpleModule
CONTAINS

    SUBROUTINE blah()
		INTEGER i
		i = blih()
    END SUBROUTINE creerGrilleVTK

	function blih()  result(i)
		INTEGER :: i
		CALL informationsElement()
	END function blih

END MODULE
