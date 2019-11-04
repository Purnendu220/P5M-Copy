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
        public String id;
        public String name;



        public String level;

        public FitnessLevel(String id, String name,String level) {
            this.id = id;
            this.name = name;
            this.level = level;
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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public static class PriceModel {
        public String id;
        public String name;
        public String priceModel;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Boolean status;

        public PriceModel(String id, String name, String priceModel) {
            this.id = id;
            this.name = name;
            this.priceModel = priceModel;
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

        public String getPriceModel() {
            return priceModel;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPriceModel(String priceModel) {
            this.priceModel = priceModel;
        }

    }

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
