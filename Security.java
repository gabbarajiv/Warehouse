public class Security {

    public boolean verifyPassword(String userID, String password, int state) {
        return verifyPasswordHidden(userID, password, state);
    }

    private boolean verifyPasswordHidden(String userID, String password, int state) {
        if (state == 0) {
            if (userID.equals("manager") && password.equals("manager")) {
                return true;
            }
        } else if (state == 1) {
            if (userID.equals("clerk") && password.equals("clerk"))
                return true;
        } else if (state == 2) {
            return (userID.equals(password));
        }
        return false;
    }

}