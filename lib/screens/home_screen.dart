import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart' hide Response;
import 'package:html_unescape/html_unescape.dart';
import 'package:hive_flutter/hive_flutter.dart';

import '../models/category.dart';
import '../models/post.dart';
import '../screens/about_app.dart';
import '../screens/detail_post.dart';
import '../screens/post_by_category.dart';
import '../screens/login.dart';
import '../services/category.dart';
import '../utils/logger.dart';
import '../services/post.dart';
import '../utils/theme_manager.dart';
import '../widgets/load_image.dart';
import '../widgets/loading_indicator.dart';
import '../widgets/post_card.dart';

class HomeScreen extends StatefulWidget {
  HomeScreen({Key? key}) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final int topPost = 5;

  int? totalPage = 1;
  int currentPage = 1;

  bool isLoading = false;

  List<Post>? listPost;
  List<Category>? listCategory;

  ScrollController? controller;

  Future fetchPost() async {
    try {
      isLoading = true;
      if (mounted) setState(() {});
      await getPost(currentPage, 15, hasEnvelope: true).then((res) {
        if (res is List<Post>) {
          listPost = res;
        } else if (res is Response) {
          if (listPost == null) {
            listPost = postFromJson(res.data['body']);
          } else {
            listPost!.addAll(postFromJson(res.data['body']));
          }
          print(currentPage);
          totalPage = res.data['headers']['X-WP-TotalPages'];
          currentPage++;
        }

        if (mounted) setState(() {});
      });
    } catch (e) {
      logger.e(e);
    } finally {
      isLoading = false;
      if (mounted) setState(() {});
    }
  }

  Future fetchCategory() async {
    try {
      await getCategories(1, 30).then((res) {
        if (res is List<Category>) {
          listCategory = res;
        } else if (res is Response) {
          listCategory = categoriesFromJson(res.data['body']);
        }
        if (mounted) setState(() {});
      });
    } catch (e) {
      logger.e(e);
    }
  }

  void _scrollListener() {
    // print(controller.position.extentAfter);
    print(currentPage);
    if (controller!.position.extentAfter < 250 &&
        currentPage <= totalPage! &&
        !isLoading) {
      fetchPost();
    }
  }

  @override
  void initState() {
    super.initState();
    fetchPost();
    fetchCategory();

    controller = ScrollController()..addListener(_scrollListener);
  }

  @override
  void dispose() {
    super.dispose();
    controller!.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: <Widget>[
          IconButton(
            icon: Icon(
              globalTheme == GlobalTheme.dark
                  ? Icons.toggle_on
                  : Icons.toggle_off,
              size: 30,
              color:
                  globalTheme == GlobalTheme.dark ? Colors.white : Colors.black,
            ),
            onPressed: () {
              setTheme(globalTheme == GlobalTheme.dark
                  ? GlobalTheme.light
                  : GlobalTheme.dark);
              setState(() {});
            },
          ),
          IconButton(
            icon: Icon(
              Icons.info,
              color:
                  globalTheme == GlobalTheme.dark ? Colors.white : Colors.black,
            ),
            onPressed: () => Get.to(() => AboutAppScreen()),
          ),
          IconButton(
            icon: Icon(
              Icons.logout,
              color:
                  globalTheme == GlobalTheme.dark ? Colors.white : Colors.black,
            ),
            onPressed: () {
              Hive.box('prefs').put('isLoggedIn', false);
              Get.offAll(() => LoginScreen());
            },
          ),
        ],
        title: Text(
          'IMAZINE',
          style: TextStyle(
            fontFamily: 'strasua',
            fontSize: 20,
            color: Colors.amber,
            fontWeight: FontWeight.w900,
          ),
        ),
      ),
      body: listPost == null
          ? loadingIndicator()
          : SafeArea(
              child: ListView(
                controller: controller,
                padding: EdgeInsets.only(top: 0),
                children: <Widget>[
                  buildCategories(),
                  buildHeader('Latest Post'),
                  buildLatestPosts(),
                  buildHeader('All Posts'),
                  buildPosts(),
                  Container(
                    padding: EdgeInsets.only(bottom: 15),
                    child: isLoading ? loadingIndicator() : Container(),
                  ),
                ],
              ),
            ),
    );
  }

  Widget buildCategories() {
    return Container(
      height: 60,
      child: listCategory == null
          ? loadingIndicator()
          : ListView.builder(
              padding: EdgeInsets.fromLTRB(25, 10, 10, 10),
              itemCount: listCategory?.length ?? 0,
              shrinkWrap: true,
              scrollDirection: Axis.horizontal,
              itemBuilder: (BuildContext context, int index) {
                Category item = listCategory![index];

                return item.count == 0
                    ? Container()
                    : Padding(
                        padding: const EdgeInsets.only(right: 20),
                        child: ElevatedButton(
                          onPressed: () => Get.to(
                              () => PostByCategoryScreen(category: item)),
                          child: Text(
                            '${item.name!.toUpperCase()}',
                            style: TextStyle(
                              fontFamily: 'Montserrat',
                              fontSize: 12,
                              fontWeight: FontWeight.w600,
                              letterSpacing: 1.5,
                              color: globalTheme == GlobalTheme.dark
                                  ? Colors.white
                                  : Colors.black,
                            ),
                          ),
                          style: ElevatedButton.styleFrom(
                            primary: globalTheme == GlobalTheme.dark
                                ? Colors.black12
                                : Colors.grey[350],
                          ),
                        ),
                      );
              },
            ),
    );
  }

  ListView buildPosts() {
    return ListView.separated(
      separatorBuilder: (context, index) {
        return Divider(height: 50);
      },
      padding: EdgeInsets.fromLTRB(20, 25, 25, 25),
      shrinkWrap: true,
      physics: NeverScrollableScrollPhysics(),
      itemCount: listPost!.sublist(topPost).length,
      itemBuilder: (context, index) {
        Post item = listPost![index + topPost];

        return PostCard(item: item);
      },
    );
  }

  Widget buildLatestPosts() {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(crossAxisAlignment: CrossAxisAlignment.start, children: [
        SizedBox(width: 10),
        ...listPost!.sublist(0, topPost).map((item) {
          return GestureDetector(
            onTap: () {
              Get.to(() => DetailScreen(item: item));
            },
            child: Container(
              width: 250,
              margin: EdgeInsets.all(15),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  SizedBox(
                    height: 200,
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(10),
                      child: Hero(
                        tag: item.jetpackFeaturedMediaUrl!,
                        child: loadImage(
                          item.jetpackFeaturedMediaUrl,
                          isShowLoading: false,
                        ),
                      ),
                    ),
                  ),
                  SizedBox(height: 15),
                  Text(
                    item.embedded!.wpTerm![0][0].name!.toUpperCase(),
                    style: TextStyle(
                      fontFamily: 'OpenSans',
                      fontSize: 11,
                      letterSpacing: 2,
                      color: Colors.grey,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  SizedBox(height: 10),
                  Container(
                    width: double.maxFinite,
                    child: Text(
                      HtmlUnescape().convert(item.title!.rendered!),
                      style: TextStyle(
                        fontFamily: 'Montserrat',
                        fontSize: 16.5,
                        fontWeight: FontWeight.w700,
                      ),
                      maxLines: 3,
                      softWrap: true,
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                ],
              ),
            ),
          );
        }).toList(),
        SizedBox(width: 10),
      ]),
    );
  }

  Padding buildHeader(String text) {
    return Padding(
      padding: EdgeInsets.fromLTRB(30, 15, 30, 0),
      child: Text(
        text,
        style: TextStyle(
          fontFamily: 'Montserrat',
          fontSize: 15,
          color: Colors.grey,
          fontWeight: FontWeight.w800,
        ),
      ),
    );
  }
}
