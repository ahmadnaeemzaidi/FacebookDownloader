package com.techgeeks.facebook.downloader.interfaces;


import com.techgeeks.facebook.downloader.model.FBStoryModel.NodeModel;
import com.techgeeks.facebook.downloader.model.story.TrayModel;

public interface UserListInterface {
    void userListClick(int position, TrayModel trayModel);
    void fbUserListClick(int position, NodeModel trayModel);
}
