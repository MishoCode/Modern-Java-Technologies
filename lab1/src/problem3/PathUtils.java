package problem3;

public class PathUtils {
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
