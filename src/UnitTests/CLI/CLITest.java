package UnitTests.CLI;

import Devices.Devices.Router;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @Test
    public void cli_question_mark(){
        Router router = new Router("router", 1);
        router.execute_command("?");
        System.out.println(router.get_monitor());
        router.clear_monitor();
        router.execute_command("enable");
        router.execute_command("?");
        System.out.println(router.get_monitor());
    }

}