package com.lcc.minispring.core.io;

import java.io.InputStream;

public interface Resource {

    InputStream getInputStream() throws Exception;
}
