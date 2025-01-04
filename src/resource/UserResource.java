package resource;

import model.User;

public final class UserResource extends Resource {
    private static final User _user = (User) readObject(FileResource.getUserFile());
    public static User getUser() {
        return _user;
    }
    private UserResource() {}
}
