package com.mmk;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SimpleIncrementorTest extends SinglethreadIncrementorTest {
    @Override
    Incrementor createNewIncrementorInstance() {
         return new SimpleIncrementor();
    }
}
