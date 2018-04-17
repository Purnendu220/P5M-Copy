package www.gymhop.p5m.data;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.utils.AppConstants;

/**
 * Created by Varun John on 4/16/2018.
 */

public class UserPackageInfo {

    public boolean havePackages = true;

    public boolean haveGeneralPackage = false;
    public boolean haveDropInPackage = false;
    public int dropInPackageCount = 0;

    public UserPackage userPackageGeneral = null;
    public List<UserPackage> userPackageReady = null;

    public UserPackageInfo(User user) {

        if (user.getUserPackageDetailDtoList() != null && !user.getUserPackageDetailDtoList().isEmpty()) {
            userPackageReady = new ArrayList<>(user.getUserPackageDetailDtoList().size());

            for (UserPackage userPackage : user.getUserPackageDetailDtoList()) {
                if (userPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                    haveGeneralPackage = true;
                    userPackageGeneral = userPackage;
                } else if (userPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                    haveDropInPackage = true;
                    userPackageReady.add(userPackage);
                    dropInPackageCount++;
                }
            }
        } else {
            havePackages = false;
        }

    }
}
