package www.gymhop.p5m.data;

/**
 * Created by Varun John on 4/5/2018.
 */

public class Filter {

    public static class Time {
        public String name;

        public Time(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Gender {
        public String name;

        public Gender(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
