package www.gymhop.p5m.eventbus;

import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.main.ClassModel;

public class Events {

    public static class UserUpdate {
        User data;

        public UserUpdate(User data) {
            this.data = data;
        }
    }

    public static class ClassJoin {
        ClassModel data;

        public ClassJoin(ClassModel data) {
            this.data = data;
        }
    }

    public static class PackagePurchased {}
}
