package kr.hhplus.be.server.domain.user;

public class UserCommand {

    public static class Create{

        private String name;

        public Create(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
