package com.p5m.me.eventbus;

import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.TrainerModel;

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

    public static class NotificationCountUpdated {
    }

    public static class UpdateUpcomingClasses {
    }

    public static class UpdatePackage {
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
