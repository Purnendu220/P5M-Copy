package com.p5m.me.data;

import java.io.Serializable;

/**
 * Created by Varun John on 4/5/2018.
 */

public class Filter implements Serializable {

    public static class Time {
        public String id;
        public String name;

        public Time(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class FitnessLevel {
        /**
         * id : 1
         * name : Basic
         * name_ar : مبتدئ
         * level : BASIC
         */

        private String id;
        private String name;
        private String name_ar;
        private String level;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_ar() {
            return name_ar;
        }

        public void setName_ar(String name_ar) {
            this.name_ar = name_ar;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

   /* public static class PriceModel {

        *//**
         * id : 4
         * name : Group
         * name_ar : مجموعة
         * priceModel : CHARGABLE
         * status : false
         *//*

        public PriceModel(String id, String name) {
            this.id = id;
            this.name = name;
        }

        private String id;
        private String name;
        private String name_ar;
        private String priceModel;
        private boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_ar() {
            return name_ar;
        }

        public void setName_ar(String name_ar) {
            this.name_ar = name_ar;
        }

        public String getPriceModel() {
            return priceModel;
        }

        public void setPriceModel(String priceModel) {
            this.priceModel = priceModel;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }*/

    public static class Gender implements Serializable {
        public String id;
        public String name;

        public Gender(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Gym implements Serializable {
        public String id;
        public String name;

        public Gym(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
