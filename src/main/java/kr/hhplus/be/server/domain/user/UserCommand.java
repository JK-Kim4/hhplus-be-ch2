package kr.hhplus.be.server.domain.user;

public class UserCommand {

    public static class Response {

        private User user;

        public Response(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}
