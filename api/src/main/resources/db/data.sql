insert into member (created_at, updated_at, email, nickname, password, role, game_count, round_count, average_cpm,
                    average_accuracy,
                    status)
values (NOW(), NOW(), 'test1@naver.com', '박영재', '{bcrypt}$2a$10$jSTH3yTOzqXr14LYOz.BJ.gCjCvDOeTKaNoJ2/qyxlFXB1ii7r8dC',
        'ROLE_USER', 0, 0, 0, 0, 'ACTIVE'),
       (NOW(), NOW(), 'test2@naver.com', '김대휘', '{bcrypt}$2a$10$jSTH3yTOzqXr14LYOz.BJ.gCjCvDOeTKaNoJ2/qyxlFXB1ii7r8dC',
        'ROLE_USER', 0, 0, 0, 0, 'ACTIVE');

INSERT INTO game_question (question, game_type)
VALUES ('리액트', 'WORD'),
       ('스프링', 'WORD'),
       ('코틀린', 'WORD'),
       ('자바', 'WORD'),
       ('파이썬', 'WORD'),
       ('노드', 'WORD'),
       ('익스프레스', 'WORD'),
       ('뷰', 'WORD'),
       ('앵귤러', 'WORD'),
       ('HTML', 'WORD'),
       ('CSS', 'WORD'),
       ('자바스크립트', 'WORD'),
       ('타입스크립트', 'WORD'),
       ('부트스트랩', 'WORD'),
       ('SASS', 'WORD'),
       ('레스', 'WORD'),
       ('웹팩', 'WORD'),
       ('바벨', 'WORD'),
       ('NPM', 'WORD'),
       ('YARN', 'WORD'),
       ('ESLint', 'WORD'),
       ('GIT', 'WORD'),
       ('깃허브', 'WORD'),
       ('비주얼코드', 'WORD'),
       ('인텔리제이', 'WORD'),
       ('이클립스', 'WORD'),
       ('안드로이드', 'WORD'),
       ('iOS', 'WORD'),
       ('코코아', 'WORD'),
       ('스위프트', 'WORD'),
       ('오브젝트C', 'WORD'),
       ('플러터', 'WORD'),
       ('다트', 'WORD'),
       ('코로나', 'WORD'),
       ('유니티', 'WORD'),
       ('언리얼', 'WORD'),
       ('C#', 'WORD'),
       ('C++', 'WORD'),
       ('루비', 'WORD'),
       ('PHP', 'WORD'),
       ('펄', 'WORD'),
       ('라라벨', 'WORD'),
       ('심포니', 'WORD'),
       ('케이크PHP', 'WORD'),
       ('스프링부트', 'WORD'),
       ('마이바티스', 'WORD'),
       ('하이버네이트', 'WORD'),
       ('JPA', 'WORD'),
       ('젠킨스', 'WORD'),
       ('도커', 'WORD'),
       ('쿠버네티스', 'WORD'),
       ('앤서블', 'WORD'),
       ('테라폼', 'WORD'),
       ('AWS', 'WORD'),
       ('GCP', 'WORD'),
       ('애저', 'WORD'),
       ('오라클', 'WORD'),
       ('SQL', 'WORD'),
       ('MySQL', 'WORD'),
       ('Redis', 'WORD'),
       ('인메모리캐시', 'WORD'),
       ('로그스태시', 'WORD'),
       ('키바나', 'WORD'),
       ('그라파나', 'WORD'),
       ('제이유닛', 'WORD'),
       ('셀레니움', 'WORD'),
       ('캐스퍼JS', 'WORD'),
       ('모카', 'WORD'),
       ('제스', 'WORD'),
       ('카르마', 'WORD'),
       ('프로트랙터', 'WORD'),
       ('스토리북', 'WORD'),
       ('사이프레스', 'WORD'),
       ('포스트맨', 'WORD'),
       ('스웨거', 'WORD'),
       ('아폴로', 'WORD'),
       ('그래프QL', 'WORD'),
       ('REST', 'WORD'),
       ('SOAP', 'WORD'),
       ('멀티쓰레드', 'WORD'),
       ('비동기', 'WORD'),
       ('프로미스', 'WORD'),
       ('옵저버블', 'WORD'),
       ('리덕스', 'WORD'),
       ('몹엑스', 'WORD'),
       ('컨텍스트', 'WORD'),
       ('훅', 'WORD'),
       ('클래스', 'WORD'),
       ('프로토타입', 'WORD'),
       ('이벤트루프', 'WORD'),
       ('웹소켓', 'WORD'),
       ('웹RTC', 'WORD'),
       ('SVG', 'WORD'),
       ('캔버스', 'WORD'),
       ('웹GL', 'WORD'),
       ('애니메이션', 'WORD'),
       ('트랜지션', 'WORD'),
       ('플렉스', 'WORD'),
       ('그리드', 'WORD'),
       ('반응형', 'WORD'),
       ('어댑티브', 'WORD'),
       ('SEO', 'WORD'),
       ('액세서빌리티', 'WORD'),
       ('i18n', 'WORD'),
       ('L10n', 'WORD'),
       ('퍼포먼스', 'WORD'),
       ('최적화', 'WORD'),
       ('메모리릭', 'WORD'),
       ('캐시', 'WORD'),
       ('로드밸런싱', 'WORD'),
       ('CDN', 'WORD'),
       ('SSL', 'WORD'),
       ('HTTPS', 'WORD'),
       ('도메인', 'WORD'),
       ('DNS', 'WORD'),
       ('라우팅', 'WORD'),
       ('미들웨어', 'WORD'),
       ('CORS', 'WORD'),
       ('JWT', 'WORD'),
       ('OAuth', 'WORD'),
       ('SAML', 'WORD');

INSERT INTO tikitaza.game_question (created_at, updated_at, question, game_type)
VALUES (NOW(), NOW(), '삶이 있는 한 희망은 있다.', 'SENTENCE'),
       (NOW(), NOW(), '산다는것 그것은 치열한 전투이다.', 'SENTENCE'),
       (NOW(), NOW(), '언제나 현재에 집중할수 있다면 행복할 것이다.', 'SENTENCE'),
       (NOW(), NOW(), '신은 용기있는자를 결코 버리지 않는다.', 'SENTENCE'),
       (NOW(), NOW(), '우리를 향해 열린 문을 보지 못하게 된다.', 'SENTENCE'),
       (NOW(), NOW(), '피할수 없으면 즐겨라.', 'SENTENCE'),
       (NOW(), NOW(), '행복한 삶을 살기위해 필요한 것은 거의 없다.', 'SENTENCE'),
       (NOW(), NOW(), '한번의 실패와 영원한 실패를 혼동하지 마라.', 'SENTENCE'),
       (NOW(), NOW(), '내일은 내일의 태양이 뜬다.', 'SENTENCE'),
       (NOW(), NOW(), '피할수 없으면 즐겨라.', 'SENTENCE'),
       (NOW(), NOW(), '계단을 밟아야 계단 위에 올라설수 있다.', 'SENTENCE'),
       (NOW(), NOW(), '행복은 습관이다, 그것을 몸에 지니라.', 'SENTENCE'),
       (NOW(), NOW(), '자신감 있는 표정을 지으면 자신감이 생긴다.', 'SENTENCE'),
       (NOW(), NOW(), '나이가 성숙을 보장하지는 않는다.', 'SENTENCE'),
       (NOW(), NOW(), '꿈은 내일의 현실이다.', 'SENTENCE'),
       (NOW(), NOW(), '한 사람에게서 모든 덕을 구하지 말라.', 'SENTENCE'),
       (NOW(), NOW(), '개발에 마지막은 없다. 출시만이 있을 뿐이다.', 'SENTENCE'),
       (NOW(), NOW(), '발없는 오픈소스 천리간다.', 'SENTENCE'),
       (NOW(), NOW(), '한 줄 코드라도 서로 거들면 낫다.', 'SENTENCE'),
       (NOW(), NOW(), '아니쓴 코드에 오류날까?', 'SENTENCE'),
       (NOW(), NOW(), '디버깅 한 번으로 천 버그 잡는다.', 'SENTENCE'),
       (NOW(), NOW(), '버그 찾기는 사막에서 바늘찾기', 'SENTENCE'),
       (NOW(), NOW(), '이게 안 되네..? 이게 되네..?', 'SENTENCE');

INSERT INTO game_history (accuracy, cpm, member_id, score, game_type)
VALUES (100, 100, 1, 100, 'WORD'),
       (100, 100, 1, 100, 'CODE'),
       (100, 100, 1, 100, 'WORD'),
       (100, 100, 2, 99, 'SENTENCE'),
       (99, 100, 2, 99, 'WORD');

INSERT INTO game_question (created_at, updated_at, question, game_type)
VALUES (NOW(), NOW(), 'import java.util.Arrays;
public class MinMaxFinder {
    public static void main(String[] args) {
        int[] numbers = {3, 5, 7, 2, 8, 1};
        int min = Arrays.stream(numbers).min().getAsInt();
        int max = Arrays.stream(numbers).max().getAsInt();
        System.out.println("Min: " + min + ", Max: " + max);
    }
}', 'CODE'),
       (NOW(), NOW(), 'import java.util.Arrays;
public class BubbleSort {
    public static void main(String[] args) {
        int[] numbers = {5, 3, 8, 4, 2};
        for (int i = 0; i < numbers.length - 1; i++)
            for (int j = 0; j < numbers.length - i - 1; j++)
                if (numbers[j] > numbers[j + 1]) {
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
        System.out.println(Arrays.toString(numbers));
    }
}', 'CODE'),
       (NOW(), NOW(), 'public class LinearSearch {
    public static void main(String[] args) {
        int[] numbers = {1, 4, 6, 7, 8, 9};
        int target = 7, index = -1;
        for (int i = 0; i < numbers.length; i++)
            if (numbers[i] == target) {
                index = i; break;
            }
        System.out.println("Index of " + target + ": " + index);
    }
}', 'CODE'),
       (NOW(), NOW(), 'public class BinarySearch {
    public static void main(String[] args) {
        int[] numbers = {1, 3, 4, 5, 7, 9, 10};
        int target = 5, left = 0, right = numbers.length - 1, index = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (numbers[mid] == target) { index = mid; break; }
            else if (numbers[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        System.out.println("Index of " + target + ": " + index);
    }
}', 'CODE'),
       (NOW(), NOW(), 'import java.util.HashSet;
public class DuplicateFinder {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 1};
        HashSet<Integer> set = new HashSet<>();
        boolean hasDuplicate = false;
        for (int number : numbers)
            if (!set.add(number)) { hasDuplicate = true; break; }
        System.out.println("Has duplicate: " + hasDuplicate);
    }
}', 'CODE'),
       (NOW(), NOW(), 'import java.util.Stack;
public class StackExample {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1); stack.push(2); stack.push(3);
        while (!stack.isEmpty()) System.out.println(stack.pop());
    }
}', 'CODE'),
       (NOW(), NOW(), 'import java.util.Queue;
import java.util.LinkedList;
public class QueueExample {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1); queue.add(2); queue.add(3);
        while (!queue.isEmpty()) System.out.println(queue.poll());
    }
}', 'CODE'),
       (NOW(), NOW(), 'public class InsertionSort {
    public static void main(String[] args) {
        int[] arr = {4, 3, 2, 10, 12, 1, 5, 6};
        for (int i = 1; i < arr.length; ++i) {
            int key = arr[i], j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        for (int i : arr) System.out.print(i + " ");
    }
}', 'CODE'),
       (NOW(), NOW(), 'public class SelectionSort {
    public static void main(String[] args) {
        int[] arr = {64, 25, 12, 22, 11};
        for (int i = 0; i < arr.length - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < arr.length; j++)
                if (arr[j] < arr[min_idx]) min_idx = j;
            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
        for (int i : arr) System.out.print(i + " ");
    }
}', 'CODE'),
       (NOW(), NOW(), 'public class SimpleLinkedList {
    Node head;
    static class Node {
        int data; Node next;
        Node(int d) { data = d; next = null; }
    }
    public static void main(String[] args) {
        SimpleLinkedList linkedList = new SimpleLinkedList();
        linkedList.head = new Node(1);
        Node second = new Node(2);
        Node third = new Node(3);
        linkedList.head.next = second; second.next = third;
        while (linkedList.head != null) {
            System.out.print(linkedList.head.data + " ");
            linkedList.head = linkedList.head.next;
        }
    }
}', 'CODE');

INSERT INTO tikitaza.game_question (created_at, updated_at, question, game_type)
VALUES (NOW(), NOW(), 'const bubbleSort = arr => {
    let len = arr.length;
    for (let i = 0; i < len; i++) {
        for (let j = 0; j < len - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]];
            }
        }
    }
    return arr;
};
console.log(bubbleSort([5, 3, 8, 4, 2]));', 'CODE'),
       (NOW(), NOW(), 'const binarySearch = (arr, target) => {
    let left = 0, right = arr.length - 1;
    while (left <= right) {
        const mid = Math.floor((left + right) / 2);
        if (arr[mid] === target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
};
console.log(binarySearch([1, 3, 4, 5, 7, 9, 10], 5));', 'CODE'),
       (NOW(), NOW(), 'const insertionSort = arr => {
    for (let i = 1; i < arr.length; i++) {
        let key = arr[i], j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j = j - 1;
        }
        arr[j + 1] = key;
    }
    return arr;
};
console.log(insertionSort([4, 3, 2, 10, 12, 1, 5, 6]));', 'CODE'),
       (NOW(), NOW(), 'const selectionSort = arr => {
    for (let i = 0; i < arr.length - 1; i++) {
        let minIdx = i;
        for (let j = i + 1; j < arr.length; j++) {
            if (arr[j] < arr[minIdx]) minIdx = j;
        }
        [arr[i], arr[minIdx]] = [arr[minIdx], arr[i]];
    }
    return arr;
};
console.log(selectionSort([64, 25, 12, 22, 11]));', 'CODE'),
       (NOW(), NOW(), 'let head = { data: 1, next: { data: 2, next: { data: 3, next: null } } };
function printList(node) {
    while (node !== null) {
        console.log(node.data);
        node = node.next;
    }
}
printList(head);', 'CODE'),
       (NOW(), NOW(), 'const graph = { A: ["B", "C"], B: ["A", "D", "E"], C: ["A", "F"], D: ["B"], E: ["B", "F"], F: ["C", "E"] };
function dfs(graph, start) {
    const stack = [start];
    const visited = new Set();
    while (stack.length) {
        const node = stack.pop();
        if (visited.has(node)) continue;
        console.log(node);
        visited.add(node);
        graph[node].forEach(neighbor => {
            if (!visited.has(neighbor)) stack.push(neighbor);
        });
    }
}
dfs(graph, "A");', 'CODE'),
       (NOW(), NOW(), 'const graph = { 1: [2, 3], 2: [1, 4, 5], 3: [1, 6, 7], 4: [2], 5: [2], 6: [3], 7: [3] };
function bfs(graph, start) {
    const queue = [start];
    const visited = new Set();
    visited.add(start);
    while (queue.length) {
        const node = queue.shift();
        console.log(node);
        graph[node].forEach(neighbor => {
            if (!visited.has(neighbor)) {
                visited.add(neighbor);
                queue.push(neighbor);
            }
        });
    }
}
bfs(graph, 1);', 'CODE'),
       (NOW(), NOW(), 'const graph = { "A": ["B", "C"], "B": ["A", "D", "E"], "C": ["A", "F"], "D": ["B"], "E": ["B", "F"], "F": ["C", "E"] };
function dfsRecursive(node, graph, visited = new Set()) {
    console.log(node);
    visited.add(node);
    graph[node].forEach(neighbor => {
        if (!visited.has(neighbor)) dfsRecursive(neighbor, graph, visited);
    });
}
dfsRecursive("A", graph);', 'CODE');
