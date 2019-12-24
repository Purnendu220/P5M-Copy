package com.p5m.me.data.main;

import java.util.List;

public class GymModel {

    /**
     * id : 6
     * profileImage : http://d1zfy71n00v47t.cloudfront.net/gymprofile/9ddf11a5-5d48-4b3d-b735-6adda453e3e1.png
     * profileImageThumbnail : http://d1zfy71n00v47t.cloudfront.net/gymprofile/9ddf11a5-5d48-4b3d-b735-6adda453e3e1_thumbnail_.png
     * trainerBranchResponseList : [{"branchId":1,"localityName":"Sharq","localityId":104,"branchName":"Al Hamra tower ","address":"Al Hamra Tower, Al-Shuhada St, Al Kuwayt, Kuwait","latitude":29.3790539,"longitude":47.9932879,"gender":"FEMALE","phoneNumber":"","gymName":"P5M Classes","gymId":6},{"branchId":74,"localityName":"Mirqab","localityId":94,"branchName":"Phase 2, AlShaheed Park","address":"Al Soor Gardens 3, Kuwait City, Kuwait","latitude":29.3640164,"longitude":47.98896690000004,"gender":"MIXED","gymName":"P5M Classes","gymId":6},{"branchId":77,"localityName":"Mirqab","localityId":94,"branchName":"Phase 1, AlShaheed Park","address":"Soor St, Al Kuwayt, Kuwait","latitude":29.3692744,"longitude":47.9933992,"gender":"MIXED","gymName":"P5M Classes","gymId":6},{"branchId":89,"localityName":"Rumaithiya","localityId":57,"branchName":"Rumaithiya ","address":"Mohammad Al Shayji elementary school ","latitude":29.31285999999999,"longitude":48.059580900000014,"gender":"MIXED","gymName":"P5M Classes","gymId":6},{"branchId":100,"localityName":"Mirqab","localityId":94,"branchName":"Elite Academy ","address":"Discovery Mall, Kuwait City, Kuwait","latitude":29.3606839,"longitude":47.9822962000001,"gender":"MALE","phoneNumber":"90967655","gymName":"P5M Classes","gymId":6},{"branchId":103,"localityName":"Shuwaikh","localityId":105,"branchName":"Shuwaikh Parking lot ","address":"Al Sadaqah & Al Salam Park, Jamal Abdul Nasser Street, Kuwait","latitude":29.3512416,"longitude":47.9496535000001,"gender":"MIXED","phoneNumber":"50005259","gymName":"P5M Classes","gymId":6},{"branchId":104,"localityName":"Salwa","localityId":60,"branchName":"Beach Sports Facilities ","address":"Beach Sports facilities, Salwa, Kuwait","latitude":29.2781391,"longitude":48.0888695,"gender":"MIXED","phoneNumber":"50005259","gymName":"P5M Classes","gymId":6},{"branchId":105,"localityName":"Shuwaikh","localityId":105,"branchName":"Shuwaikh Park","address":"Shuwaikh Park","latitude":29.3523817,"longitude":47.9503589999999,"gender":"MIXED","phoneNumber":"","gymName":"P5M Classes","gymId":6}]
     * gymNames : P5M Classes
     */

    private int id;
    private String profileImage;
    private String profileImageThumbnail;
    private String gymNames;
    private List<TrainerBranchResponseListBean> trainerBranchResponseList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageThumbnail() {
        return profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getGymNames() {
        return gymNames;
    }

    public void setGymNames(String gymNames) {
        this.gymNames = gymNames;
    }

    public List<TrainerBranchResponseListBean> getTrainerBranchResponseList() {
        return trainerBranchResponseList;
    }

    public void setTrainerBranchResponseList(List<TrainerBranchResponseListBean> trainerBranchResponseList) {
        this.trainerBranchResponseList = trainerBranchResponseList;
    }

    public static class TrainerBranchResponseListBean {
        /**
         * branchId : 1
         * localityName : Sharq
         * localityId : 104
         * branchName : Al Hamra tower
         * address : Al Hamra Tower, Al-Shuhada St, Al Kuwayt, Kuwait
         * latitude : 29.3790539
         * longitude : 47.9932879
         * gender : FEMALE
         * phoneNumber :
         * gymName : P5M Classes
         * gymId : 6
         */

        private int branchId;
        private String localityName;
        private int localityId;
        private String branchName;
        private String address;
        private double latitude;
        private double longitude;
        private String gender;
        private String phoneNumber;
        private String gymName;
        private int gymId;

        public int getBranchId() {
            return branchId;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public String getLocalityName() {
            return localityName;
        }

        public void setLocalityName(String localityName) {
            this.localityName = localityName;
        }

        public int getLocalityId() {
            return localityId;
        }

        public void setLocalityId(int localityId) {
            this.localityId = localityId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getGymName() {
            return gymName;
        }

        public void setGymName(String gymName) {
            this.gymName = gymName;
        }

        public int getGymId() {
            return gymId;
        }

        public void setGymId(int gymId) {
            this.gymId = gymId;
        }
    }
}
