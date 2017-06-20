package Assignments;

import java.io.IOException;

import org.jointheleague.ecolban.rpirobot.IRobotAdapter;
import org.jointheleague.ecolban.rpirobot.IRobotInterface;
import org.jointheleague.ecolban.rpirobot.SimpleIRobot;

public class AssignmentCode extends IRobotAdapter {
	Sonar sonar = new Sonar();

	public AssignmentCode(IRobotInterface iRobot) {
		super(iRobot);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Try event listner, rev Monday 2030");
		IRobotInterface base = new SimpleIRobot();
		AssignmentCode rob = new AssignmentCode(base);
		rob.setup();
		while (rob.loop()) {
		}
		rob.shutDown();

	}

	private void setup() throws Exception {
		// SETUP CODE GOES HERE!!!!!
		readSensors(100);
		getDistance();
		getAngle();
	}

	int distance = 0;
	int angle = 0;
	int fowardState = 0;
	int turnState = 1;
	int dockState = 2;
	int currentState = fowardState;

	private boolean loop() throws Exception {
		// LOOP CODE GOES HERE!!!!!
		readSensors(100);
		if (currentState == fowardState) {
			driveDirect(500, 500);
			distance = distance + getDistance();
			if (distance > 200) {
				currentState = turnState;
				angle = 0;

			}
		} else if (currentState == turnState) {
			driveDirect(200, -200);
			angle = angle + getAngle();
			System.out.println(angle);
			if (angle < -360) {
				currentState = fowardState;
				distance = 0;
			}
			
			if(getInfraredByteLeft() > 0||getInfraredByteRight() > 0){
				currentState=dockState;
			
			}

		} else if (currentState == dockState) {

			if (getInfraredByteLeft() > 0) {
				driveDirect(100, 200);
				sleep(100);
			} else if (getInfraredByteRight() > 0) {
				driveDirect(200, 100);
				sleep(100);
			} else {
				driveDirect(500, 500);
			}
		}


		if (isHomeBaseChargerAvailable()) {
			driveDirect(0, 0);
			return false;
		}

		return true;
	}

	private void sleep(int amt) {
		try {
			Thread.sleep(amt);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void shutDown() throws IOException {
		reset();
		stop();
		closeConnection();
	}
}
