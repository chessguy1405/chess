package dataaccess;

import model.UserData;
import java.util.Collection;

public interface ReadUserData {
    Collection<UserData> getUsers();
}
