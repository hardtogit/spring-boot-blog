package com.company.project.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

import java.util.List;

/**
 * Created by thon on 8/30/14.
 */
public class JsoupUtil {
    /**
     * 截取字符串长字，保留HTML格式
     *
     * @param content
     * @param len     字符长度
     */
    public static String truncateHTML(String content, int len) {
        Document dirtyDocument = Jsoup.parse(content);
        Element source = dirtyDocument.body();
        int initLen = source.text().length();

        Document clean = Document.createShell(dirtyDocument.baseUri());
        Element dest = clean.body();
        truncateHTML(source, dest, len, initLen);

        if(dest.text().length() < initLen){
            dest.append("… <a class=\"btn-link toggle-expand\">显示全部</a>");
        }
        return dest.outerHtml().replace("<body>", "").replace("</body>", "");
    }

    /**
     * 截取字符串长字，忽略HTML格式
     *
     * @param content
     * @param len     字符长度
     */
    public static String truncatEscapeHTML(String content, int len) {
        Document dirtyDocument = Jsoup.parse(content);
        Element source = dirtyDocument.body();
        Document clean = Document.createShell(dirtyDocument.baseUri());
        Element dest = clean.body();
        truncateHTML(source, dest, len, source.text().length());
        return dest.text();
    }

    /**
     * 使用Jsoup预览
     *
     * @param source 需要过滤的
     * @param dest   过滤后的对象
     * @param len    截取字符长度
     *               eg.<br />
     *               <p/>
     *               Document dirtyDocument = Jsoup.parse(sb.toString());<br />
     *               Element source = dirtyDocument.body();<br />
     *               Document clean = Document.createShell(dirtyDocument.baseUri());<br />
     *               Element dest = clean.body();<br />
     *               int len = 6;<br />
     *               truncateHTML(source,dest,len);<br />
     *               System.out.println(dest.html());<br />
     */
    private static void truncateHTML(Element source, Element dest, int len, int initLen) {
        List<Node> sourceChildren = source.childNodes();
        for (Node sourceChild : sourceChildren) {
            if (sourceChild instanceof Element) {
                Element sourceEl = (Element) sourceChild;
                Element destChild = createSafeElement(sourceEl);
                int txt = dest.text().length();
                if (txt >= len) {
                    break;
                } else {
                    len = len - txt;
                }
                dest.appendChild(destChild);
                truncateHTML(sourceEl, destChild, len, initLen);
            } else if (sourceChild instanceof TextNode) {
                int destLeng = dest.text().length();
                if (destLeng >= len) {
                    break;
                }
                TextNode sourceText = (TextNode) sourceChild;
                int txtLeng = sourceText.getWholeText().length();
                if ((destLeng + txtLeng) > len) {
                    int tmp = len - destLeng;
                    String txt = sourceText.getWholeText().substring(0, tmp);
                    TextNode destText = new TextNode(txt, sourceChild.baseUri());
                    dest.appendChild(destText);
                    break;
                } else {
                    TextNode destText = new TextNode(sourceText.getWholeText(), sourceChild.baseUri());
                    dest.appendChild(destText);
                }
            }
        }
    }

    /**
     * 按原Element重建一个新的Element
     *
     * @param sourceEl
     * @return
     */
    private static Element createSafeElement(Element sourceEl) {
        String sourceTag = sourceEl.tagName();
        Attributes destAttrs = new Attributes();
        Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
        Attributes sourceAttrs = sourceEl.attributes();
        for (Attribute sourceAttr : sourceAttrs) {
            destAttrs.put(sourceAttr);
        }
        return dest;
    }

    public static String clean(String unsafe) {
        String safe = Jsoup.clean(unsafe, Whitelist.basic());
        return safe;
    }

    public static void main(String[] args) throws Exception {
//        Document doc = Jsoup.connect("http://jsoup.org").get();
//        System.out.println(doc.html());
//        System.out.println("-----------------------------------------");
//        System.out.println(JsoupUtil.truncateHTML(doc.html(), 400));

        //String content = "<p>我认为这不是真的在说谎。</p><p>四岁孩子都会有这样的行为，但不能因此说孩子会说谎了。</p><p>我们也不需要干涉。</p><p>无为而治。</p><p>想想看，他是不是在模仿大人的回答？</p><p>而且他能把这个事情讲得如此精彩。</p><p><br/></p>";
        String content ="<p></p> <p><strong>什么是关系的教育</strong></p> <p><strong>关于父母的自我教育和帮助孩子与人和万物连结</strong></p> <p></p> <p>    有一天，我拿起很早之前买的一本幼儿教育的书，这本书我从来没有翻开过。翻开后我看到这段话：孩子们生活在自然环境中，生活在人文环境中，生活在生活环境中，也生活在音乐、美术、语言、数学中。生活在所有的事物中。但是，这些事物只是一个存在，不是孩子成长的根本。这里有一个巨大的秘密储存在孩子的世界里。那就是，如果说这个外在的环境可以对孩子的生命发生作用的话，那一定是孩子的心灵和某一事物产生了触碰或者连结，连结产生了关系，关系使孩子拥有了环境，环境发挥了作用。这样的连接才会使生命有所变化。连结上了，一切才变得有意义。而最伟大的连结是人与人的连结，我们需要守候在孩子的身边。</p> <p></p> <p>    孙瑞雪说：我期盼孩子们就生活在与万物连结的环境中，与人连结的环境中。这就是我心中的教育。</p> <p></p> <p>    是的，连结。是的，关系。上周同儿子幼儿园老师和园长的谈话中，我们也谈到了连结和关系，但谈论的主角不是孩子而是父母。</p> <p></p> <p>    这些年，我们从各个渠道听到、看到和感受到:家庭教育才是孩子所受的教育中起决定性作用的教育。</p> <p></p> <p>    家庭教育中每天都在上演的，但大多数父母没有意识到的就是自己与家人的关系，与他人的关系，与这个外部世界的关系，以及自己和自己的关系。</p> <p></p> <p>    我在这里说的“关系”和我们不假思索认为的“关系”有一些不同。为此我先来说说我们不假思索认为的那种“关系”。多年以来，我们听到一个说法：中国人办事靠的就是关系。小时候，我对这句话的理解是，走后门很容易把事办成，关系就是走后门。所以我对“关系”这个词怀有敌对情绪。“关系”变成了“个人利益关系”的简称。</p> <p></p> <p>   </p> <p></p> <p>    后来，在一次面试中，我开始重新理解“关系”这个词。对方问我：你和谁的关系最不好？我不假思索回答，和老公的关系最不好。面试之后对方再无消息，我等待了很久之后，慢慢回过味来。发现在当时的状态下，那份工作确实不适合我。 </p> <p></p> <p>   夫妻关系是所有关系中最重要的关系。</p> <p></p> <p>   一个家庭中如果夫妻关系不好，父母与孩子关系也好不到哪里去。没有良好的亲子关系，家庭教育就不知从何说起。试想一下，我们自己童年生活在一个吵吵闹闹的家庭里，父母每天都在为生活琐事怄气，互相指责，有时候还把气撒在我们身上。我们是什么感受？最初是害怕，逐渐变得冷漠，和父母一样爱挑别人的毛病，遇到问题总是归罪于别人。在父母的吵闹声中，孩子没有安全感，无法全身心的与外部世界建立联系。孩子不可能从连夫妻关系都处理不好的父母那里学会好好与他人相处与自己相处。</p> <p></p> <p>    最近一个月我听到两个朋友说起了3例中学生跳楼的事件，还从一个做心理咨询的老师那里听说，他在一年之内接到了9例有严重心理疾病和神经分裂情况的孩子，他们只有十二三岁！这些孩子都怎么了？</p> <p></p> <p>    大家都说，一定是教育出了问题！</p> <p></p> <p>    教育出了什么问题？原因在哪里？大家可能会说社会环境不利于孩子成长，学校教育越来越不合理。还是不断从外面找原因。为什么不从内部找找原因呢？</p> <p></p> <p>    外部原因似乎一看即明，大家都可以站出来说上几句，仅仅是说上几句能让这些原因得到改善吗？要想改善，还得从内部找原因，从每个人的起点找原因。每个人的起点在哪里？自然是在家庭里。</p> <p></p> <p>    大多数的教育都是针对孩子的，殊不知最需要教育的是父母。现在大多数的学校也挂起了家长学校的牌子，据我所知，这些家长学校是校方教家长如何配合学校的，他们不断给父母提要求，把一些过去应该由学校做的事情推到家长那里，还明里暗里的鼓励家长送孩子去培训学校补习，很少涉及父母的自我成长。这样的家长学校等同于家长的负担或者说是紧箍咒。谁让你的孩子在人家手里呢！父母只好各种配合。</p> <p></p> <p>    你想没想过去破除这个魔咒？如果想破除，该怎么破除？</p> <p></p> <p>    其实这个魔咒不攻自破，你只要清楚是谁在为孩子的成长负责，谁最关切孩子的未来。不是学校！是父母！搞清楚这个你就能想明白很多事情。孩子的成长和教育既然主要在家庭，那我们就把家庭搞好，让自己的家庭环境更加有利于孩子的成长。</p> <p></p> <p>    如果你只想仰赖于学校教育，那你一定不会看到我的这篇文章。既然你看到这篇文章了，那我们就来说说家庭教育。</p> <p></p> <p>    其实，家庭教育的起点是父母的自我教育。自我教育的起点是处理好各种关系，尤其是夫妻关系。夫妻关系其实是你与自己的关系。这个话说得象绕口令，里面却蕴藏着奥秘。想一想，我们成家以后和谁的关系最紧密？自然是另一半。这种亲密关系会让我们在他们面前最放松，会用最自然的方式来对待他们。如果我们习惯于挑对方的毛病，那实际上是我们自己内在没有得到认同，小时候也是不断被人挑毛病，受批评。如果我们小时候没有受到尊重，我们也不懂得如何尊重对方。如果我们小时候没有获得安全感，我们可能一辈子也放松不下来，去信任对方。</p> <p></p> <p>所有的问题都可以从我们的小时候找到原因。如果你不想让自己的孩子长大后面对和你一样的问题。那我们就疗愈自己吧！</p> <p></p> <p>疗愈自己的方法很多，我这篇文章只讲夫妻关系。</p> <p></p> <p>因为孩子最亲密的两个成人应该是父母，父母之间的关系决定了家庭的氛围，孩子每天都在学习父母之间的相处方式，然后变成自己与他人的相处方式。</p> <p></p> <p>什么是好的夫妻关系不用我在这里说了，大家都比较清楚。“世界上的幸福都是一样的，而不幸是各有各的不幸。”</p> <p></p> <p>我只举例说明不好的夫妻关系。</p> <p></p> <p>妈妈太强势，什么事都压着爸爸，当着孩子的面数落丈夫，是第一大忌。反过来爸爸太强势也一样。夫妻之间要互相尊重，温和。</p> <p></p> <p>爸爸妈妈各干各的，没有任何共同爱好或者活动。住在一个家里，却互不关心，是第二大忌。这种冷暴力的杀伤性很强。夫妻之间要相互关心，给予对方温暖。</p> <p></p> <p>爸爸妈妈轮流数落对方家庭的不是，是第三大忌。不接纳对方的家庭就是不接纳对方，没有宽容之心，家庭就不象温暖的港湾了。</p> <p></p> <p>还有很多，家家都一本难念的经。</p> <p></p> <p>根本都在于我们能不能原谅对方，包容对方，关心对方，尊重对方。</p> <p></p> <p>我的家庭犯过这三条大忌，所以我与丈夫的关系一度非常紧张。现在我们关系很好，彼此尊重，相互理解。</p> <p></p> <p>这样的结果是孩子的状态越来越好。他变得乐群，懂得关心小朋友。玩耍的时候更放松，释放出他的想象力和创造力。</p> <p></p> <p>给孩子营造温暖的家庭氛围不需要你花钱购买漂亮的沙发，也不需要你为了家人付出你的全部心血。需要的是你享受这种温暖，并且给大家带来这种温暖。好好的爱着自己，然后去爱家里人，就会自然而然的得到温暖的家庭氛围。</p> <p></p> <p>所以，所以，请你好好的爱自己。因为所有的关系都是和自己的关系。</p> <p></p> <p> </p> <p></p>";
        content = JsoupUtil.truncateHTML(content, 112311);
        System.out.println(content);

//        String unsafe = "<h3 style=\"font-size: 14px; outline: 0px; margin: 0px; color: rgb(34, 34, 34); font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; white-space: normal;\">答案总结</h3><ol style=\"padding: 0px; margin-top: 0.8em; margin-bottom: 0.8em; margin-left: 2em; list-style-position: outside;\" class=\" list-paddingleft-2\"><li><p>认真：认真对待你的提问，就像你希望别人认真回答你的问题一样。<br/></p></li><li><p>先搜索：确保你提了一个新问题，而不是别人问过的重复问题（解决办法：提问之前先搜索）。<br/></p></li><li><p>具体：让你的问题处于具体的环境中，避免大而空洞、需要具体情况来分析、或别人难以读懂的问题。解决办法：将问题阐述清楚，尽可能将问题的「补充说明」写清楚。<br/></p></li><li><p>书写：正确地使用标点符号和英文大小写，没有错别字。<br/></p></li><li><p>添加话题：给问题添加合适的相关话题。<br/></p></li><li><p>邀请回答：使用右侧的「站内邀请」，邀请专业的人来回答。</p></li><li><p>在问题中避免出现绝对性词汇。很多问题你认为是这样但事实并不如此。</p></li><li><p>避免在问题中加上自己对答案非专业或非正确的猜想。</p></li></ol><p><br/></p>";
//        String safe = Jsoup.clean(unsafe, Whitelist.basic());
//        System.out.println(safe);
//        System.out.println(Jsoup.clean(null, Whitelist.basic()));
    }
}
