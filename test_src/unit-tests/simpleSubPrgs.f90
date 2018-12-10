MODULE simpleModule
CONTAINS

    SUBROUTINE blah()
		INTEGER i
		i = blih()
 		i = i+1
    END SUBROUTINE blah

	function blih()  result(i)
		INTEGER :: i
		CALL blah()
		i = 0
	END function blih

END MODULE
