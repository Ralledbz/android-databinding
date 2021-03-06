## Samples

   <img src="/databinding_1.gif" alt="Demo Screen Capture" width="300px" />
   <img src="/databinding_2.gif" alt="Demo Screen Capture" width="300px" />
   <img src="/imgs/databinding_3.gif" alt="Demo Screen Capture" width="300px" />
   
   before build the sample project,you need to import the xml schema file of this databind framework.
   see <b>[How to use plugin and xml schema ?]</b>
   
## databind config  detail

``` java
<DataBinding
    xmlns = "http://schemas.android.com/heaven7/android-databinding/1"
    version="1.0"
    >
    <data>
        <!-- define the a bean data , decided by type = 'bean' -->
        <variable name="user"  classname="com.heaven7.databinding.demo.bean.User"  type="bean"/>
        <!-- define the a event variable , decided by type = 'callback' -->
        <variable name="mainHanlder" classname="com.heaven7.databinding.demo.callback.MainEventHandler" type="callback"/>
        <!-- import a class and then you can use alias to call the static methods and fields.
           such as: View.VISIBLE  and etc.
           the property of alias can be hide.  that mean's alias is the string'classname.substring(classname.lastIndexOf(".")+1)'
           -->
        <import classname="android.view.View" alias="View"/>
    </data>

    <!-- bind multi properties to  the view(id = 'bt') -->
    <bind id="bt">
        <property name="text" referVariable="user" >@{user.username}</property>
        <property name="textColor" referVariable="user" >user.male ? {@color/red} : {@color/random}</property>
    </bind>

    <!-- bind a data to multi views  -->
    <bind referVariable="user">
        <!-- bind the  value of expression to the text of view  which id = 'bt2' -->
        <property id ="bt2" name="text" >@{user.username}</property>
        <property id ="bt3" name="text" >user.getNickname()</property>
    </bind>

    <bind id="iv">
        <!-- old -->
        <property name="img_url" referVariable="user">@{user.link}</property>

        <!-- image full config -->
        <imageProperty type="round" referVariable="user">  <!-- type must be :  round / circle / oval -->

             <roundSize >{@dimen/xxx}</roundSize>           <!-- roundSize define the four corners size of image  -->

            <corners >                                      <!-- corners equals to  roundSize  -->
                 <topRight>{@dimen/xxx}</topRight>          <!-- the top right corner size -->
                 <topLeft>{@dimen/xxx}</topLeft>            <!-- the top left corner size -->
                 <bottomLeft>{@dimen/xxx}</bottomLeft>      <!-- the bottom left corner size -->
                 <bottomRight>{@dimen/xxx}</bottomRight>    <!-- the bottom right corner size -->
             </corners>

             <borderWidth>{@dimen/xxx}</borderWidth>       <!-- the border width  -->
             <borderColor>{@color/xxx}</borderColor>       <!-- the border color  -->

            <url>@{user.link}</url>                       <!-- the network url of image -->
            <default>@{xxx}</default>        <!-- default image . support drawable ,bitmap,  resource id -->
            <errorResId>@{xxx}</errorResId>  <!-- error image . only support resource id -->

        </imageProperty>
    </bind>

    <!-- bean class must implements the interface ISelectable in bind adapter  -->
    <!-- 选择模式select mode: selectMode="1"means single , 2  means multi -->
    <bindAdapter id="lv" referVariable="user" selectMode="1">
        <item layout="item_xxx" tag = "1" referVariable="itemHandler">
            <!-- bind item root onLongClick event -->
            <property name="onLongClick" >itemHandler.onItemLongClick(user)</property>
            <!-- bin item root onClick event -->
            <property name="onClick" >itemHandler.onItemClick(user)</property>
            <bind id="bt">
                <property name="text" >user.username</property>
                <property name="textColor" >user.isSelected() ? {@color/red} : {@color/random}</property>
                <property name="onClick" referVariable="orderHandler">orderHandler.onClickName(user)</property>
            </bind>
        </item>
        <item layout="item_xxx2" tag="2"  >
            <bind >
                <property id ="bt2" name="text" >@{user.username}</property>
                <property id ="bt3" name="text" >user.getNickname()</property>
            </bind>
            <bind id="bt">
                <property name="text" >user.username</property>
                <property name="onClick" referVariable="orderHandler">orderHandler.onClickName(user)</property>
            </bind>
            <bind id="iv">
                <property name="img_url" >@{user.link}</property>
                <imageProperty type="round">  <!-- round / circle / oval -->
                    <roundSize >{@dimen/xxx}</roundSize>
                    <corners >
                        <topRight>{@dimen/xxx}</topRight>
                        <topLeft>{@dimen/xxx}</topLeft>
                        <bottomLeft>{@dimen/xxx}</bottomLeft>
                        <bottomRight>{@dimen/xxx}</bottomRight>
                    </corners>

                    <borderWidth>{@dimen/xxx}</borderWidth>
                    <borderColor>{@color/xxx}</borderColor>

                    <url>@{user.link}</url>
                    <default>@{xxx}</default>        <!-- support drawable ,bitmap,  resource id -->
                    <errorResId>@{xxx}</errorResId>  <!-- only support resource id -->

                </imageProperty>
            </bind>

        </item>
    </bindAdapter>


</DataBinding>

```

## Usage   

base BaseBehaviour.

``` java
//1, write some of your code
//the event handler called by databinding
public class MainEventHandler extends EventContext{

    public MainEventHandler(IDataBinder binder) {
        super(binder);
    }

    public void onClickChangeUsername(View v,User user){
        Util.changeUserName(user,"by_MainEventHandler_OnClick");
        getDataBinder().notifyDataSetChanged(R.id.bt);
    }

    public void onLongClickChangeUsername(View v,User user){
        Toast t =  Toast.makeText(v.getContext(), "------------ onLongClick ---------", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
        Util.changeUserName(user,"by_MainEventHandler_OnLongClick");
        getDataBinder().notifyDataSetChanged(R.id.bt, PropertyNames.TEXT);
    }
}
//2. define databinding xml below raw folder,like this 

<DataBinding>
    <data>
        <variable name="user"  classname="com.heaven7.databinding.demo.bean.User"  type="bean"/>
        <variable name="mainHanlder" classname="com.heaven7.databinding.demo.callback.MainEventHandler" type="callback"/>
        <import classname="android.view.View" alias="View"/>
    </data>

    <bind id="bt">
        <property name="text" referVariable="user">@{user.username}</property>
        <property name="textColor" referVariable="user" >user.male ? {@color/red} : {@color/random}</property>
    </bind>
    <bind id="bt0">
        <property name="onClick" referVariable="user,mainHanlder" >mainHanlder.onClickChangeUsername(user)</property>
        <property name="onLongClick" referVariable="user,mainHanlder" >mainHanlder.onLongClickChangeUsername(user)</property>
    </bind>

    <bind variable="user">
        <property id ="bt2" name="text"  valueType="string">@{user.username}</property>
        <property id ="bt3" name="text" >user.getNickname()</property>
    </bind>


</DataBinding>

//3,call databinding in java code , eg: in onCreate()...
 private void doBind() {
        //init DataBinder
        mDataBinder = new DataBinder(this, R.raw.databinding_main);

        //bind a User and cache it for latter call notify.
        mDataBinder.bind(R.id.bt, true, mUser = new User("heaven7", false));

        //bind onClick event and onLongClick event and not cache any data
        mDataBinder.bind(R.id.bt0, false, mUser,new MainEventHandler(mDataBinder));

        //bind a data to multi views. but cache
        mDataBinder.bind(new User("joker", true,"xxx_joker"));
    }

```
below is bind adapter 
``` java
//1, declare your main layout  like this
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/lv"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </ListView>

</LinearLayout>

// 2, declare item layout like this 
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <com.android.volley.extra.ExpandNetworkImageView
        android:id="@+id/eniv"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:scaleType="centerCrop"
        />

    <TextView
        android:id="@+id/tv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:padding="10dp"
        />

</LinearLayout>

//3 , declare the config of bind adapter like this:
<DataBinding>
    <data>
        <variable name="imageInfo"  classname="com.heaven7.databinding.demo.bean.ImageInfo"  type="bean"/>
        <variable name="itemHandler" classname="com.heaven7.databinding.demo.samples.ListViewBindAdapterTest$ItemHandler"
            type="callback"/>
    </data>

    <!-- bean must implements the interface ISelectable in bind adapter  -->
    <!-- selectMode="1"means single , 2  means multi , if not declared default is 1 -->

    <bindAdapter id="lv" referVariable="imageInfo">
        <item layout="item_image"  referVariable="itemHandler">
            <!-- root onClickListener-->
            <property name="onClick" >itemHandler.onItemClick()</property>
            <bind id="tv">
                <property name="text" >imageInfo.desc</property>
                <property name="textColor" >imageInfo.isSelected() ? {@color/red} : {@color/random}</property>
                <property name="onClick" >itemHandler.onTextClick()</property>
            </bind>
            <bind id="eniv">
                <property name="img_url" >imageInfo.url</property>
            </bind>
        </item>
    </bindAdapter>

</DataBinding>

//4,  java code -> bindAdapter
public class ListViewBindAdapterTest extends BaseActivity {

    AdapterManager<ImageInfo> mAM;

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_simple_listview;
    }
    @Override
    protected int getlayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
    }

    @Override
    public void doBind() {
        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            infos.add(new ImageInfo(Test.URLS[i],"desc_"+i));
        }
        mAM = mDataBinder.bindAdapter(R.id.lv, infos, new ItemHandler(getToaster()));
    }

    public static class ItemHandler {
        private final Toaster mToaster;
        public ItemHandler(Toaster mToaster) {
            this.mToaster = mToaster;
        }

        //every param must not be primitive
        public void onItemClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
        }
        public void onTextClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("on text click: position = " + position + " ,item = " + item);
        }
    }
}

```

bind multi item in adapter
``` java

//1, first declare layout xml , like this:

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/lv"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </ListView>

</LinearLayout>

//2, bind xml config like this: 
//you should care about the ImageInfo class must implement ITag interface. it will used to bind multi items.

<DataBinding>
    <data>
        <variable name="imageInfo"  classname="com.heaven7.databinding.demo.bean.ImageInfo"  type="bean"/>
        <variable name="itemHandler" classname="com.heaven7.databinding.demo.samples.MultiItemAdapterTest$ItemHandler2"
            type="callback"/>
    </data>

    <bindAdapter id="lv" referVariable="imageInfo" selectMode="1">

        <item layout="item_image"  tag = "1" referVariable="itemHandler">
            <property name="onClick" >itemHandler.onItemClick()</property>
            <bind id="tv">
                <property name="text" >imageInfo.desc</property>
                <property name="textColor" >imageInfo.isSelected() ? {@color/red} : {@color/random}</property>
                <property name="onClick" >itemHandler.onTextClick()</property>
            </bind>
            <bind id="eniv">
                <property name="img_url" >imageInfo.url</property>
            </bind>
        </item>

        <item layout="item_txt"  tag = "2" referVariable="itemHandler">
            <property name="onClick" >itemHandler.onTitleClick()</property>
            <bind id="tv">
                <property name="text" >imageInfo.title</property>
                <property name="textColor" >imageInfo.isSelected() ? {@color/green} : {@color/blue}</property>
            </bind>
        </item>

    </bindAdapter>
</DataBinding>

//3, relative java code 
 // (1) the imageInfo class like this:
/**
 * as the item of bind adapter . ImageInfo must implement ISelectable interface.
 * if multi item. must implement ITag interface
 * Created by heaven7 on 2015/11/30.
 */
public class ImageInfo implements ISelectable,ITag{

    private String url;

    private String desc;

    private String title;

    private boolean selected;
    private int tag ;

    public ImageInfo(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public ImageInfo(String url, String desc,String title) {
        this.url = url;
        this.desc = desc;
        this.title = title;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int tag) {
        this.tag = tag;
    }
}

// (2) the sample like this:
public class MultiItemAdapterTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_multi_item_listview;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {

    }

    @Override
    protected void doBind() {
        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            final ImageInfo info = new ImageInfo(Test.URLS[i], "desc_" + i, "_title_" + i);
            info.setTag(i % 2 == 0 ? 1: 2 );
            infos.add(info);
        }
        mDataBinder.bindAdapter(R.id.lv, infos, new ItemHandler2(getToaster()));
    }
    public static class ItemHandler2 {

        private final Toaster mToaster;

        public ItemHandler2(Toaster mToaster) {
            this.mToaster = mToaster;
        }
        /** this is bind in item: item_image */
        public void onItemClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
        }
        /** this is bind in item: item_image */
        public void onTextClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("on text click: position = " + position + " ,item = " + item);
        }

        /** this is bind in item: item_txt */
        public void onTitleClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("[ this is called on item2-> 'item_txt' ] on title click: position = " +
                    position + " ,item = " + item);
        }
    }
}

``` 

below is bind image to ExpandNetworkImageView , support corner ,border,circle and so on.
   here is the main code.
``` java
 // xml conifg in like this:
 <DataBinding>
    <data>
        <variable name="imageParam" classname="com.heaven7.databinding.demo.samples.RoundImageBindTest$ImageParam" type="bean"/>
        <import classname="com.heaven7.databinding.demo.test.Test" alias="Test"/>
    </data>

    <bind id="eniv">
        <property name="img_round_builder" referVariable="imageParam" >
            Test.createRoundBuilder(imageParam.roundSize,imageParam.url)
        </property>
    </bind>
</DataBinding>

// current use expression combine java code. later will support full config in xml

 public static RoundedBitmapBuilder createRoundBuilder(float radius,String url){
        return new RoundedBitmapBuilder().url(url).cornerRadius(radius)
                .borderWidth(2f)
                .borderColor(Color.RED)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
    }
    
/**
 * later will support full config in xml
 * Created by heaven7 on 2015/12/2.
 */
public class RoundImageBindTest extends BaseActivity {

    @Override
    protected int getBindRawId() {
        return R.raw.db_round_image_test;
    }
    @Override
    protected int getlayoutId() {
        return R.layout.activity_round_image_test;
    }
    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
    }
    @Override
    protected void doBind() {
        mDataBinder.bind(R.id.eniv,false,new ImageParam(30f, Test.URLS[0]) );
    }

    public static class ImageParam{
        float roundSize;
        String url;
        public ImageParam(float roundSize, String url) {
            this.roundSize = roundSize;
            this.url = url;
        }
    }

}

``` 
   
... and so on. 

the more to see in  [android-databinding/sample](https://github.com/LightSun/android-databinding/tree/master/Android-databinding/sample).

thanks !
