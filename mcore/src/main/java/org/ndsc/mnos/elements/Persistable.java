package org.ndsc.mnos.elements;

import java.util.Map;

/**
 * Created by cgz on 12/27/16.
 */
public interface Persistable {
    public Map<String, Object> getDBIndex();

    public String getDBKey();

    public String getDBName();

    public Map<String, Object> getDBObject();

}
