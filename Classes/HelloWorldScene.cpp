#include "HelloWorldScene.h"

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include "jni.h"
#include "Platform/android/jni/JniHelper.h"
#endif

USING_NS_CC;

const char g_szPackageName[] = "org/cocos2dx/cpp/AppActivity";

HelloWorld::HelloWorld() : Layer(), m_pEnterActivityItem(nullptr)
{

}

HelloWorld::~HelloWorld()
{

}

Scene* HelloWorld::createScene()
{
	CCLOG("createScene");
	
    // 'scene' is an autorelease object
    auto scene = Scene::create();
    
    // 'layer' is an autorelease object
    auto layer = HelloWorld::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !Layer::init() )
    {
        return false;
    }
    
    Size visibleSize = Director::getInstance()->getVisibleSize();
    Vec2 origin = Director::getInstance()->getVisibleOrigin();

    /////////////////////////////
    // 2. add a menu item with "X" image, which is clicked to quit the program
    //    you may modify it.

    // add a "close" icon to exit the progress. it's an autorelease object
    auto closeItem = MenuItemImage::create(
                                           "CloseNormal.png",
                                           "CloseSelected.png",
                                           CC_CALLBACK_1(HelloWorld::menuCloseCallback, this));
    
	closeItem->setPosition(Vec2(origin.x + visibleSize.width - closeItem->getContentSize().width/2 ,
                                origin.y + closeItem->getContentSize().height/2));

	m_pEnterActivityItem = MenuItemImage::create("Button_Home_Normal.png", "Button_Home_Press.png", CC_CALLBACK_1(HelloWorld::menuCloseCallback, this));
	if (m_pEnterActivityItem)
	{
		m_pEnterActivityItem->setPosition(Vec2(visibleSize.width / 2, visibleSize.height - 60.f));
	}

    // create menu, it's an autorelease object
    auto menu = Menu::create(closeItem, m_pEnterActivityItem, NULL);
    menu->setPosition(Vec2::ZERO);
    this->addChild(menu, 1);

    /////////////////////////////
    // 3. add your codes below...

    // add a label shows "Hello World"
    // create and initialize a label
    
    auto label = Label::createWithTTF("Hello World", "fonts/Marker Felt.ttf", 24);
    
    // position the label on the center of the screen
    label->setPosition(Vec2(origin.x + visibleSize.width/2,
                            origin.y + visibleSize.height - label->getContentSize().height));

    // add the label as a child to this layer
    this->addChild(label, 1);

    // add "HelloWorld" splash screen"
    auto sprite = Sprite::create("HelloWorld.png");

    // position the sprite on the center of the screen
    sprite->setPosition(Vec2(visibleSize.width/2 + origin.x, visibleSize.height/2 + origin.y));

    // add the sprite as a child to this layer
    this->addChild(sprite, 0);
    
    return true;
}


void HelloWorld::menuCloseCallback(Ref* pSender)
{
	CCLOG("HelloWorld::menuCloseCallback: Start 1");

	MenuItemImage* pGameItemBtn = dynamic_cast<MenuItemImage*>(pSender);
	if (pGameItemBtn == m_pEnterActivityItem)
	{
		CCLOG("HelloWorld::menuCloseCallback: Start");

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
		bool bHasMethod;
		JniMethodInfo info;
		
		bHasMethod = JniHelper::getStaticMethodInfo(info, g_szPackageName, "Share", "()V");
		if (bHasMethod)
		{
			if (info.methodID)
			{
				info.env->CallStaticVoidMethod(info.classID, info.methodID);
			}
			else
			{
				CCLOG("function share was not called");
			}
		}
		else
		{
			CCLOG("function share was not found");
		}
#endif

		CCLOG("HelloWorld::menuCloseCallback: End");
	}
	else
	{
		Director::getInstance()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
		exit(0);
#endif
	}
}
