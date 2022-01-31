Feature: TuringMachine must be present and be executable
	Scenario: TuringMachine must be found
		When I successfully run `getexecutable` 
		Then 10 points are awarded
		And a file named "../../bin/turingmachine" should exist
		Then 10 points are awarded

