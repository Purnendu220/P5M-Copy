package com.p5m.me.eventbus;

import com.p5m.me.data.BookWithFriendData;
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

    public static class ClassRating {
        public String classRating;

        public ClassRating(String classRating) {
            this.classRating = classRating;
        }

    }

    public static class BookWithFriend {
        public BookWithFriendData friendData;

        public BookWithFriend(BookWithFriendData friendData) {
            this.friendData = friendData;
        }

    }

    public static class ClassAutoJoin {
        public ClassModel classModel;

        public ClassAutoJoin(ClassModel classModel) {
            this.classModel = classModel;
        }
    }

    public static class PackagePurchased {
    }

    public static class NewFilter {
    }

    public static class RefreshClassList {
    }

    public static class NotificationCountUpdated {
    }

    public static class UpdateUpcomingClasses {
    }

    public static class UpdatePackage {
    }

    public static class refreshMembershipFragment {
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

    public static class WaitlistItemRemoved {
        public ClassModel data;

        public WaitlistItemRemoved(ClassModel data) {
            this.data = data;
        }
    }

    public static class WaitlistJoin {
        public ClassModel data;

        public WaitlistJoin(ClassModel data) {
            this.data = data;
        }
    }

    public static class WishRemoved {
        public ClassModel data;

        public WishRemoved(ClassModel data) {
            this.data = data;
        }
    }

    public static class ExploreApiUpdate {
    }

    public static class BannerUrlHandler {
        public int innerTab;

        public BannerUrlHandler(int data) {
            this.innerTab = data;
        }
    }
    public static class ChangeCountry {

        public ChangeCountry() {
        }

    }

    public static class OnUserCountChange{
        public int userCount;
        public OnUserCountChange(int userCount) {
            this.userCount=userCount;
        }

    }
    public static class OnUserDisconnectedCall{
        public OnUserDisconnectedCall() {
        }

    }
}
