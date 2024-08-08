package webserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpTypeTest {

    @Test
    public void isPostTrue() {
        assertTrue(HttpType.POST.isPost());
    }

    @Test
    public void isPostFalse() {
        assertFalse(HttpType.GET.isPost());
    }
}