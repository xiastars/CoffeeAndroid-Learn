package com.summer.demo.ui.course;

import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.course.fragment.VideoGetCoverFragment;

/**
 * @Description: Fragment容器
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 11:44
 */
public class CourseContainerActivity extends FragmentContainerActivity {

    @Override
    protected void showViews(int type) {
        switch (type) {
            case MarkdownPos.JAVA_OBJECT:
                setTitle("关于对象");
                showFragment(MarkDownFragment.show("detail/learnjava1.txt"));
                break;
            case MarkdownPos.JAVA_CHILD:
                setTitle("子类与父类");
                showFragment(MarkDownFragment.show("detail/learnjava2.txt"));
                break;
            case MarkdownPos.NET_FIVE_STRUCTRURE:
                setTitle("五层体系结构");
                String url = "file:///android_asset/net/fivestructure.html";
                showFragment(CourseWebFragment.show(url));
                break;
            case MarkdownPos.NET_HANKSHAKE:
                setTitle("TCP三次握手和四次挥手过程");
                url = "file:///android_asset/net/handshake.html";
                showFragment(CourseWebFragment.show(url));
                break;
            case MarkdownPos.NET_HTTPS:
                setTitle("Http和Https");
                url = "file:///android_asset/net/httpandhttps.html";
                showFragment(CourseWebFragment.show(url));
                break;
            case MarkdownPos.TOOL_VIDEO_FRAME:
                setTitle("生成封面");
                showFragment(new VideoGetCoverFragment());
                break;
        }
    }

}
