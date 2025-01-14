package frc.robot.commands.Shooter;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Swerve;
import frc.robot.Constants.Shooter;
import edu.wpi.first.wpilibj2.command.Command;


public class AutoVisionShoot extends Command {    
    private double ty;
    private double ta;
    private double ySpeed;
    private double xSpeed;
    private Swerve s_Swerve;    


    private double yShooterAngle = 0;
    private double aShooterAngle = 0;
    private double shooterAngle = 0;


    public AutoVisionShoot(Swerve s_Swerve) {
        this.s_Swerve = s_Swerve;

        addRequirements(RobotContainer.frontLimelight);
        addRequirements(RobotContainer.leftShooter);
        addRequirements(RobotContainer.rightShooter);
        addRequirements(RobotContainer.shooterWrist);
    }

    public void initialize() {
        ty = RobotContainer.frontLimelight.getY();
        ta = RobotContainer.frontLimelight.getArea();
    }
    
    @Override
    public void execute() {
        // find target location
        ty = RobotContainer.frontLimelight.getY();
        ta = RobotContainer.frontLimelight.getArea();

        xSpeed = s_Swerve.xSpeed()*1.75;  // does this need to be larger


        // Uses ta to set shooter angle
        // Eggo (-17.3601*ta*ta) + (41.5424*ta) - (4); // - 2.82088
        aShooterAngle = (-10.3868*ta*ta) + (34.2588*ta) - (0.8); // - 1.22507

        // use ty to calculate shooter angle
        // Eggo (-0.009811884*ty*ty) + (0.740631*ty) + (17);  // 18.3463
        yShooterAngle = (-0.0024887*ty*ty) + (0.785214*ty) + (20.2);  // 19.4856

        // average data from both equations
        shooterAngle = ((aShooterAngle + yShooterAngle) / 2) - xSpeed;  // is this going the right way and is it the right value

        // disallow any negative values
        if (shooterAngle <= 0) {
            shooterAngle = 0;
        }

        else{

        }

        // Sets Shooter angle and speed
        RobotContainer.leftShooter.setTargetVelocity(Shooter.DefaultShotVelocity.VelocityLeft);
        RobotContainer.rightShooter.setTargetVelocity(Shooter.DefaultShotVelocity.VelocityRight);

        // Controls shooter speed and angle
        RobotContainer.leftShooter.velocityControl();
        RobotContainer.rightShooter.velocityControl();

        RobotContainer.shooterWrist.setTargetPosition(shooterAngle);
        RobotContainer.shooterWrist.motionMagicControl();

    }
}
