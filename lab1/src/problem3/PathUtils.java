package problem3;

public class PathUtils {
    /*public static String[] simplifyPath(String[] tokens) {
        int k = 0;
        String[] directories = new String[tokens.length];
        for (String s : tokens) {
            if (!s.equals(".")) {
                directories[k] = s;
                k++;
            }

        }

        return directories;
    }

    public static String getCanonicalPath(String path) {
        StringBuilder canonicalPath = new StringBuilder("/");
        String[] tokens = path.split("/+");
        String[] directories = simplifyPath(tokens);

        int lastDirectoryLen = 0, i = 1;
        while (i < directories.length && directories[i] != null) {
            if (directories[i].equals("..")) { // go to the previous directory
                if (lastDirectoryLen > 0) { // remove the previous directory name
                    canonicalPath.delete(canonicalPath.length() - lastDirectoryLen - 1, canonicalPath.length());
                    lastDirectoryLen = i > 2 ? directories[i - 2].length() : 0;
                }
            } else if (!directories[i].equals(".")) { //directory name found
                canonicalPath.append(directories[i]); // add the new directory name
                if (i != directories.length - 1) {
                    canonicalPath.append("/");
                }
                lastDirectoryLen = directories[i].length();
            }
            i++;
        }

        if (canonicalPath.length() > 1 && canonicalPath.charAt(canonicalPath.length() - 1) == '/') {
            canonicalPath.delete(canonicalPath.length() - 1, canonicalPath.length());
        }

        return canonicalPath.toString();
    }*/

    public static String getCanonicalPath(String path) {
        StringBuilder canonicalPath = new StringBuilder();
        String[] directories = path.split("/+");
        int skipCount = 0;

        for (int i = directories.length - 1; i >= 1; i--) {
            if (!directories[i].equals(".")) {
                if (directories[i].equals("..")) {
                    skipCount++;
                } else if (skipCount == 0) {
                    canonicalPath.insert(0, '/' + directories[i]);
                } else {
                    skipCount--;
                }
            }
        }

        return canonicalPath.isEmpty() ? canonicalPath.append('/').toString() : canonicalPath.toString();
    }

    public static void main(String[] args) {
        String path = "/home/./alabala////../jdk/a/..";
        System.out.println(getCanonicalPath(path));

        String path1 = "/home/";
        System.out.println(getCanonicalPath(path1));

        String path2 = "/../";
        System.out.println(getCanonicalPath(path2));

        String path3 = "/home//foo/";
        System.out.println(getCanonicalPath(path3));

        String path4 = "/a/./b/../../c/";
        System.out.println(getCanonicalPath(path4));

        String path5 = "/.././c/abc/..";
        System.out.println(getCanonicalPath(path5));

        String path6 = "/./";
        System.out.println(getCanonicalPath(path6));

        String path7 = "/.././././c/d/e/../";
        System.out.println(getCanonicalPath(path7));

        String path8 = "/abc/xyz/./d/../../dfd/.";
        System.out.println(getCanonicalPath(path8));

        String path9 = "/abc/xyz/alabalaaa/../../";
        System.out.println(getCanonicalPath(path9));

        String path10 = "/.///..././";
        System.out.println(getCanonicalPath(path10));
    }
}
