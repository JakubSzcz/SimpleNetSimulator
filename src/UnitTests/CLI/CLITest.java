package UnitTests.CLI;

import Devices.Devices.Router;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    @Test
    public void cli_question_mark(){
        Router router = new Router("router", 1);
        StringBuilder expected = new StringBuilder();
        // disable
        router.execute_command("?");
        for (String word : router.get_all_commands().get("disable")){
            expected.append(word).append("\n");
        }
        expected.delete(0, 2);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());

        // disable
        router.clear_monitor();
        expected = new StringBuilder();
        router.execute_command("enable");
        router.execute_command("?");

        for (String word : router.get_all_commands().get("enable")){
            expected.append(word).append("\n");
        }
        expected.delete(0, 2);
        expected.append("\n");

        assertEquals(expected.toString(), router.get_monitor());
    }

}