import hudson.util.VersionNumber

/**
 * Determines if VersionB is *only* higher in the patch version than VersionA
 * @param versionA version string A
 * @param versionB version string B
 * @return true if the patch version of VersionB > VersionA
 */
boolean isPatchVersionUpgrade(String versionA, String versionB) {

    def versionNumberA = new VersionNumber(versionA)
    def versionNumberB = new VersionNumber(versionB)

    if (versionNumberB.isOlderThanOrEqualTo(versionNumberA)) {
        return false
    }

    if (isMajorVersionGreater(versionNumberA, versionNumberB)) {
        return false
    }

    if (isMinorVersionGreater(versionNumberA, versionNumberB)) {
        return false
    }

    return isPatchVersionGreater(versionNumberA, versionNumberB)
}

private static boolean isMajorVersionGreater(VersionNumber versionNumberA, VersionNumber versionNumberB) {

    def majorVersionA = getVersionDigitFromIndex(versionNumberA, VersionConstants.MAJOR_VERSION_INDEX)
    def majorVersionB = getVersionDigitFromIndex(versionNumberB, VersionConstants.MAJOR_VERSION_INDEX)

    if (majorVersionB > majorVersionA) {
        return true
    }

    return false
}

private static int getVersionDigitFromIndex(VersionNumber versionNumber, int index) {

    def digit = versionNumber.getDigitAt(index)

    return digit != -1 ? digit : 0
}

private static boolean isMinorVersionGreater(VersionNumber versionNumberA, VersionNumber versionNumberB) {

    def minorVersionA = getVersionDigitFromIndex(versionNumberA, VersionConstants.MINOR_VERSION_INDEX)
    def minorVersionB = getVersionDigitFromIndex(versionNumberB, VersionConstants.MINOR_VERSION_INDEX)

    if (minorVersionB > minorVersionA) {
        return true
    }

    return false
}

private static boolean isPatchVersionGreater(VersionNumber versionNumberA, VersionNumber versionNumberB) {

    def patchVersionA = getVersionDigitFromIndex(versionNumberA, VersionConstants.PATCH_VERSION_INDEX)
    def patchVersionB = getVersionDigitFromIndex(versionNumberB, VersionConstants.PATCH_VERSION_INDEX)

    if (patchVersionB > patchVersionA) {
        return true
    }

    return false
}

/**
 * Determines if VersionB is *only* higher in the minor version than VersionA
 * @param versionA version string A
 * @param versionB version string B
 * @return true if the minor version of VersionB > VersionA
 */
boolean isMinorVersionUpgrade(String versionA, String versionB) {

    def versionNumberA = new VersionNumber(versionA)
    def versionNumberB = new VersionNumber(versionB)

    if (versionNumberB.isOlderThanOrEqualTo(versionNumberA)) {
        return false
    }

    if (isMajorVersionGreater(versionNumberA, versionNumberB)) {
        return false
    }

    return isMinorVersionGreater(versionNumberA, versionNumberB)
}

/**
 * Determines if VersionB is *only* higher in the major version than VersionA
 * @param versionA version string A
 * @param versionB version string B
 * @return true if the major version of VersionB > VersionA
 */
boolean isMajorVersionUpgrade(String versionA, String versionB) {

    def versionNumberA = new VersionNumber(versionA)
    def versionNumberB = new VersionNumber(versionB)

    if (versionNumberB.isOlderThanOrEqualTo(versionNumberA)) {
        return false
    }

    return isMajorVersionGreater(versionNumberA, versionNumberB)
}

class VersionConstants {
    static final Integer MAJOR_VERSION_INDEX = 0
    static final Integer MINOR_VERSION_INDEX = 1
    static final Integer PATCH_VERSION_INDEX = 2
}
