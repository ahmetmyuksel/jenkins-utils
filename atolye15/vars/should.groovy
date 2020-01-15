def test(String branchName) {
    if (branchName.startsWith("hotfix") || branchName.startsWith("feature")) {
        echo "INFO: ${branchName} should not be tested.";
        return false;
    }
    echo "INFO: Project Branch ${branchName} should be tested.";
    return true;
}

def pack(String branchName) {

    def packageableBranches = ["master", "develop", "release", "legacy"] as List<String>;

    for (branch in packageableBranches) {
        if (branchName.startsWith(branch)) {
            echo "INFO: ${branchName} should be packaged.";
            return true;
        }
    }

    if (branchName.startsWith("PR")) {
        echo "INFO: Pull Request ${branchName}";
        String source = env.CHANGE_BRANCH;
        echo "INFO: Source branch: ${source}"
        String target = env.CHANGE_TARGET;
        echo "INFO: Target branch: ${target}";
        if (checkPackagingforPR(source, target)) {
            echo "INFO: ${branchName} from ${source} to ${target} should be packaged.";
            return true;
        }
    }
    echo "INFO: Project Branch ${branchName} should not be packaged";
    return false;
}

def checkPackagingforPR(String sourceBranch, String targetBranch) {
            if ((sourceBranch.startsWith("hotfix") && targetBranch == "master") ||
                (sourceBranch.startsWith("hotfix") && targetBranch == "legacy")) {
                return true;
            }
}

def deploy(String branchName) {

    def deployableBranches = ["master", "develop", "release"] as List<String>;

    for (branch in deployableBranches) {
        if (branchName.startsWith(branch)) {
            echo "INFO: Deploy ${branchName}";
            return true;
        }
    }
    if (branchName.startsWith("PR")) {
        String sourceBranch = env.CHANGE_BRANCH;
        String targetBranch = env.TARGET_BRANCH;
        if (sourceBranch.startsWith("hotfix") && targetBranch == "master") {
            echo "INFO: ${branchName} from ${source} to ${target} should be deployed.";
            return true;
        }
        echo "INFO: Project ${branchName} should not be deployed.";
        return false;
    }
    echo "INFO: Project ${branchName} should not be deployed.";
    return false;
}
