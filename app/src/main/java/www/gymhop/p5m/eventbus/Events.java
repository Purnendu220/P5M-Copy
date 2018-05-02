package www.gymhop.p5m.eventbus;

import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerModel;

public class Events {

    public static class UserUpdate {
        User data;

        public UserUpdate(User data) {
            this.data = data;
        }
    }

    public static class ClassJoin {
        public ClassModel data;

        public ClassJoin(ClassModel data) {
            this.data = data;
        }
    }

    public static class PackagePurchased {
    }

    public static class NewFilter {
    }

    public static class PackagePurchasedForClass {
        public ClassModel data;

        public PackagePurchasedForClass(ClassModel data) {
            this.data = data;
        }
    }

    public static class ClassPurchased {
        public ClassModel data;

        public ClassPurchased(ClassModel data) {
            this.data = data;
        }
    }

    public static class TrainerFollowed {
        public TrainerModel data;
        public boolean isFollowed;

        public TrainerFollowed(TrainerModel data, boolean isFollowed) {
            this.data = data;
            this.isFollowed = isFollowed;
        }
    }

    public static class WishAdded {
        public ClassModel data;

        public WishAdded(ClassModel data) {
            this.data = data;
        }
    }

    public static class WishRemoved {
        public ClassModel data;

        public WishRemoved(ClassModel data) {
            this.data = data;
        }
    }
}
