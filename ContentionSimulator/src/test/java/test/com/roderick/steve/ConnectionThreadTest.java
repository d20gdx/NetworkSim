package test.com.roderick.steve;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.roderick.steve.netsim.ConnectionThread;

class ConnectionThreadTest {

    @Test
    public void testType() {
        ConnectionThread testClass = new ConnectionThread("Multitech",1,7);
        assertTrue(testClass instanceof ConnectionThread);
    }

    @Test
    public void testConstructorLookup_MultitechGW() {
        assertEquals(new ConnectionThread("Multitech",1, 7).getTimeOnAir(), 0.05f);
        assertEquals(new ConnectionThread("Multitech",1, 8).getTimeOnAir(), 0.14f);
        assertEquals(new ConnectionThread("Multitech",1, 9).getTimeOnAir(), 0.29f);
        assertEquals(new ConnectionThread("Multitech",1, 10).getTimeOnAir(), 1.09f);
        assertEquals(new ConnectionThread("Multitech",1, 11).getTimeOnAir(), 2.5f);
        assertEquals(new ConnectionThread("Multitech",1, 12).getTimeOnAir(), 4.23f);
        assertEquals(new ConnectionThread("Multitech",1, 99).getTimeOnAir(), -1f);
    }

    @Test
    public void testConstructorLookup_TTNGW() {
        assertEquals(new ConnectionThread("TTN",1, 7).getTimeOnAir(), 0.05f);
        assertEquals(new ConnectionThread("TTN",1, 8).getTimeOnAir(), 0.14f);
        assertEquals(new ConnectionThread("TTN",1, 9).getTimeOnAir(), 0.29f);
        assertEquals(new ConnectionThread("TTN",1, 10).getTimeOnAir(), 1.01f);
        assertEquals(new ConnectionThread("TTN",1, 11).getTimeOnAir(), 2.4f);
        assertEquals(new ConnectionThread("TTN",1, 12).getTimeOnAir(), 4.1f);
        assertEquals(new ConnectionThread("TTN",1, 99).getTimeOnAir(), -1f);
    }

}
