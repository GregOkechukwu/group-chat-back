package com.groupchatback.service;

import com.groupchatback.aws.S3ClientProvider;
import com.groupchatback.config.LocalMetaData;
import com.groupchatback.dao.interfaces.UserDao;
import com.groupchatback.util.EnvConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LocalMetaData localBean;

    private String profilePicPrefix = EnvConfigUtil.getProperty("PATH_PROFILE_PICS_PREFIX");
    private String iconPrefix = EnvConfigUtil.getProperty("PATH_ICONS_PREFIX");
    private String[] iconNames = new String[]{"profile", "message", "friends", "invitation", "profile-big", "message-big", "friends-big", "invitation-big"};

    private byte[] getImage(String key) throws IOException {
        return S3ClientProvider.getImage(key);
    }

    private void putImage(String key, byte[] byteArr, String mimeType) throws Exception {
        S3ClientProvider.putImage(key, byteArr, mimeType);
    }

    private void deleteImage(String key) throws Exception {
        S3ClientProvider.deleteImage(key);
    }

    private String getMimeType(String key) throws Exception {
        return S3ClientProvider.getMimeType(key);
    }

    public boolean hasProfilePic() {
        String decodedId = this.localBean.getDecodedUserId();
        return this.userDao.hasProfilePic(decodedId);
    }

    public String getProfilePicMimeType() throws Exception {
        String decodedId = this.localBean.getDecodedUserId();
        return this.getMimeType(profilePicPrefix + decodedId);
    }

    public String getProfilePicMimeType(String userId) throws Exception {
        return this.getMimeType(profilePicPrefix + userId);
    }

    public String getDefaultPicMimeType() throws Exception {
        return this.getMimeType(profilePicPrefix + "default");
    }

    public String getIconMimeType() throws Exception {
        return this.getMimeType(iconPrefix + iconNames[0]);
    }

    public byte[] getProfilePic() throws Exception {
        String decodedId = this.localBean.getDecodedUserId();
        return this.getImage(profilePicPrefix + decodedId);
    }

    public byte[] getProfilePic(String userId) throws Exception {
        return this.getImage(profilePicPrefix + userId);
    }

    public byte[] getDefaultPic() throws Exception {
        return this.getImage(profilePicPrefix + "default");
    }

    public void postProfilePic(String base64Str, String mimeType) throws Exception {
        String decodedId = this.localBean.getDecodedUserId();
        byte[] byteArrBase64 = DatatypeConverter.parseBase64Binary(base64Str);

        Map<String, Object> attributeLookup = new HashMap<>();
        attributeLookup.put("hasProfilePic", true);

        this.putImage(profilePicPrefix + decodedId, byteArrBase64, mimeType);
        this.userDao.updateUser(decodedId, attributeLookup);
    }

    public void deleteProfilePic() throws Exception {
        String decodedId = this.localBean.getDecodedUserId();

        Map<String, Object> attributeLookup = new HashMap<>();
        attributeLookup.put("hasProfilePic", false);

        this.deleteImage(decodedId);
        this.userDao.updateUser(decodedId, attributeLookup);
    }

    public Map<String, byte[]> getIcons() throws IOException {
        Map<String, byte[]> iconLookup = new HashMap<>();
        int n = iconNames.length;

        for (int i = 0; i < n; i++) {
            byte[] byteArr = this.getImage(iconPrefix + iconNames[i]);
            iconLookup.put(iconNames[i], byteArr);
        }

        return iconLookup;
    }

    public List<Object[]> getProfileOrDefaultPics(List<Object[]> usersRecords, int userIdIdx, int hasPicIdx) throws Exception {
        int n = usersRecords.size();
        List<Object[]> pics = new ArrayList<>(n);

        byte[] defaultPic = this.getDefaultPic();
        String defaultPicMimeType = this.getDefaultPicMimeType();

        for (int i = 0; i < n; i++) {
            Object[] picAndMimeType = new Object[2];

            if (usersRecords.get(i) == null) {
                pics.add(picAndMimeType);
                continue;
            }

            String userId = (String)usersRecords.get(i)[userIdIdx];
            boolean hasProfilePic = (boolean)usersRecords.get(i)[hasPicIdx];

            byte[] pic = hasProfilePic ? this.getProfilePic(userId) : defaultPic;
            String mimeType = hasProfilePic ? this.getProfilePicMimeType(userId) : defaultPicMimeType;

            picAndMimeType[0] = pic;
            picAndMimeType[1] = mimeType;
            pics.add(picAndMimeType);
        }

        return pics;
    }

}
