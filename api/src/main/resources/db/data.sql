insert into member (created_at, updated_at, email, nickname, password, role, game_count, round_count, average_cpm,
                    average_accuracy,
                    status)
values (NOW(), NOW(), 'test1@naver.com', '박영재', '{bcrypt}$2a$10$jSTH3yTOzqXr14LYOz.BJ.gCjCvDOeTKaNoJ2/qyxlFXB1ii7r8dC',
        'ROLE_USER', 0, 0, 0, 0, 'ACTIVE'),
       (NOW(), NOW(), 'test2@naver.com', '김대휘', '{bcrypt}$2a$10$jSTH3yTOzqXr14LYOz.BJ.gCjCvDOeTKaNoJ2/qyxlFXB1ii7r8dC',
        'ROLE_USER', 0, 0, 0, 0, 'ACTIVE');

INSERT INTO game_history (accuracy, cpm, member_id, score, game_type)
VALUES (100, 100, 1, 100, 'WORD'),
       (100, 100, 1, 100, 'CODE'),
       (100, 100, 1, 100, 'WORD'),
       (100, 100, 2, 99, 'SENTENCE'),
       (99, 100, 2, 99, 'WORD');

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

INSERT INTO game_question (question, game_type)
VALUES ('인셉션', 'WORD'),
       ('매트릭스', 'WORD'),
       ('글래디에이터', 'WORD'),
       ('인터스텔라', 'WORD'),
       ('쇼생크탈출', 'WORD'),
       ('포레스트검프', 'WORD'),
       ('쥬라기공원', 'WORD'),
       ('타이타닉', 'WORD'),
       ('아바타', 'WORD'),
       ('다크나이트', 'WORD'),
       ('해리포터', 'WORD'),
       ('스타워즈', 'WORD'),
       ('반지의제왕', 'WORD'),
       ('토이스토리', 'WORD'),
       ('업', 'WORD'),
       ('모아나', 'WORD'),
       ('프로즌', 'WORD'),
       ('아이언맨', 'WORD'),
       ('토르', 'WORD'),
       ('캡틴아메리카', 'WORD'),
       ('블랙팬서', 'WORD'),
       ('스파이더맨', 'WORD'),
       ('배트맨비긴즈', 'WORD'),
       ('원더우먼', 'WORD'),
       ('쥬만지', 'WORD'),
       ('미션임파서블', 'WORD'),
       ('제이슨본', 'WORD'),
       ('잭리처', 'WORD'),
       ('셜록홈즈', 'WORD'),
       ('인디아나존스', 'WORD'),
       ('덩케르크', 'WORD'),
       ('1917', 'WORD'),
       ('조커', 'WORD'),
       ('겟아웃', 'WORD'),
       ('어스', 'WORD'),
       ('헤로니모', 'WORD'),
       ('빌리엘리어트', 'WORD'),
       ('로마의휴일', 'WORD'),
       ('카사블랑카', 'WORD'),
       ('라라랜드', 'WORD'),
       ('무간도', 'WORD'),
       ('올드보이', 'WORD'),
       ('기생충', 'WORD'),
       ('아가씨', 'WORD'),
       ('추격자', 'WORD'),
       ('해운대', 'WORD'),
       ('괴물', 'WORD'),
       ('타짜', 'WORD'),
       ('실미도', 'WORD'),
       ('암살', 'WORD'),
       ('도둑들', 'WORD'),
       ('왕의남자', 'WORD'),
       ('서편제', 'WORD'),
       ('나는전설이다', 'WORD'),
       ('그래비티', 'WORD'),
       ('트루먼쇼', 'WORD'),
       ('빅피쉬', 'WORD'),
       ('에덴의동쪽', 'WORD'),
       ('신세계', 'WORD'),
       ('설국열차', 'WORD'),
       ('무드인디고', 'WORD'),
       ('퍼펙트센스', 'WORD'),
       ('블랙스완', 'WORD'),
       ('레옹', 'WORD'),
       ('영웅본색', 'WORD'),
       ('다이하드', 'WORD'),
       ('로보캅', 'WORD'),
       ('터미네이터2', 'WORD'),
       ('백투더퓨처', 'WORD'),
       ('에일리언2', 'WORD'),
       ('콘에어', 'WORD'),
       ('스피드', 'WORD'),
       ('트와일라잇', 'WORD'),
       ('헝거게임', 'WORD'),
       ('다이버전트', 'WORD'),
       ('메이즈러너', 'WORD');

INSERT INTO game_question (question, game_type)
VALUES ('피자', 'WORD'),
       ('스파게티', 'WORD'),
       ('라자냐', 'WORD'),
       ('햄버거', 'WORD'),
       ('치킨', 'WORD'),
       ('김치찌개', 'WORD'),
       ('불고기', 'WORD'),
       ('초밥', 'WORD'),
       ('라멘', 'WORD'),
       ('타코', 'WORD'),
       ('부리토', 'WORD'),
       ('파스타', 'WORD'),
       ('비빔밥', 'WORD'),
       ('된장찌개', 'WORD'),
       ('팔라펠', 'WORD'),
       ('훠궈', 'WORD'),
       ('딤섬', 'WORD'),
       ('커리', 'WORD'),
       ('피쉬앤칩스', 'WORD'),
       ('팟타이', 'WORD'),
       ('탄두리치킨', 'WORD'),
       ('피에야', 'WORD'),
       ('사시미', 'WORD'),
       ('크로와상', 'WORD'),
       ('바게트', 'WORD'),
       ('에스카르고', 'WORD'),
       ('쿠스쿠스', 'WORD'),
       ('포', 'WORD'),
       ('봉골레파스타', 'WORD'),
       ('샐러드', 'WORD'),
       ('갈비탕', 'WORD'),
       ('샤브샤브', 'WORD'),
       ('스테이크', 'WORD'),
       ('바비큐립', 'WORD'),
       ('나초', 'WORD'),
       ('엔칠라다', 'WORD'),
       ('퀘사디아', 'WORD'),
       ('후무스', 'WORD'),
       ('바클라바', 'WORD'),
       ('돈까스', 'WORD'),
       ('오므라이스', 'WORD'),
       ('타코야키', 'WORD'),
       ('오코노미야키', 'WORD'),
       ('김밥', 'WORD'),
       ('떡볶이', 'WORD'),
       ('순대', 'WORD'),
       ('파전', 'WORD'),
       ('장어구이', 'WORD'),
       ('곱창', 'WORD'),
       ('막창', 'WORD'),
       ('쭈꾸미볶음', 'WORD'),
       ('감바스', 'WORD'),
       ('파에야', 'WORD'),
       ('모히토', 'WORD'),
       ('리조또', 'WORD'),
       ('멘보샤', 'WORD'),
       ('마파두부', 'WORD'),
       ('캐비어', 'WORD'),
       ('푸아그라', 'WORD'),
       ('슈크림', 'WORD'),
       ('마카롱', 'WORD'),
       ('타르트', 'WORD'),
       ('에클레어', 'WORD'),
       ('크레페', 'WORD'),
       ('팬케이크', 'WORD'),
       ('와플', 'WORD'),
       ('브리오슈', 'WORD'),
       ('티라미수', 'WORD'),
       ('젤라토', 'WORD'),
       ('소르베', 'WORD'),
       ('아포가토', 'WORD'),
       ('바나나스플릿', 'WORD'),
       ('치즈케이크', 'WORD'),
       ('브라우니', 'WORD'),
       ('초콜릿무스', 'WORD'),
       ('페스토파스타', 'WORD'),
       ('까르보나라', 'WORD'),
       ('아메리카노', 'WORD'),
       ('카푸치노', 'WORD'),
       ('라떼', 'WORD'),
       ('모카', 'WORD'),
       ('에스프레소', 'WORD'),
       ('마끼아또', 'WORD'),
       ('플랫화이트', 'WORD'),
       ('코코넛워터', 'WORD'),
       ('망고스틴', 'WORD'),
       ('듀리안', 'WORD'),
       ('라이치', 'WORD'),
       ('파인애플', 'WORD'),
       ('아보카도', 'WORD'),
       ('키위', 'WORD'),
       ('그래놀라', 'WORD'),
       ('요거트', 'WORD'),
       ('스무디', 'WORD'),
       ('프로틴바', 'WORD'),
       ('오트밀', 'WORD'),
       ('퀴노아', 'WORD'),
       ('에너지바', 'WORD'),
       ('헴프시드', 'WORD'),
       ('체리', 'WORD'),
       ('블루베리', 'WORD'),
       ('아사이베리', 'WORD');

INSERT INTO game_question (question, game_type)
VALUES ('애플', 'WORD'),
       ('구글', 'WORD'),
       ('아마존', 'WORD'),
       ('MS', 'WORD'),
       ('코카콜라', 'WORD'),
       ('삼성', 'WORD'),
       ('토요타', 'WORD'),
       ('벤츠', 'WORD'),
       ('BMW', 'WORD'),
       ('인텔', 'WORD'),
       ('페이스북', 'WORD'),
       ('디즈니', 'WORD'),
       ('맥도날드', 'WORD'),
       ('나이키', 'WORD'),
       ('소니', 'WORD'),
       ('레고', 'WORD'),
       ('넷플릭스', 'WORD'),
       ('루이비통', 'WORD'),
       ('샤넬', 'WORD'),
       ('구찌', 'WORD'),
       ('프라다', 'WORD'),
       ('에르메스', 'WORD'),
       ('지방시', 'WORD'),
       ('버버리', 'WORD'),
       ('테슬라', 'WORD'),
       ('롤렉스', 'WORD'),
       ('페라리', 'WORD'),
       ('람보르기니', 'WORD'),
       ('포르쉐', 'WORD'),
       ('비자', 'WORD'),
       ('마스터카드', 'WORD'),
       ('페이팔', 'WORD'),
       ('스타벅스', 'WORD'),
       ('피자헛', 'WORD'),
       ('케이FC', 'WORD'),
       ('버거킹', 'WORD'),
       ('도미노피자', 'WORD'),
       ('오레오', 'WORD'),
       ('레드불', 'WORD'),
       ('펩시', 'WORD'),
       ('몬스터에너지', 'WORD'),
       ('캐논', 'WORD'),
       ('닌텐도', 'WORD'),
       ('엑스박스', 'WORD'),
       ('아식스', 'WORD'),
       ('언더아머', 'WORD'),
       ('뉴발란스', 'WORD'),
       ('컨버스', 'WORD'),
       ('반스', 'WORD'),
       ('스케쳐스', 'WORD'),
       ('P&G', 'WORD'),
       ('유니레버', 'WORD'),
       ('로레알', 'WORD'),
       ('에스티로더', 'WORD'),
       ('클리니크', 'WORD'),
       ('샤오미', 'WORD'),
       ('화웨이', 'WORD'),
       ('오포', 'WORD'),
       ('비보', 'WORD'),
       ('레노버', 'WORD'),
       ('HP', 'WORD'),
       ('델', 'WORD'),
       ('에이서', 'WORD'),
       ('ASUS', 'WORD'),
       ('IBM', 'WORD'),
       ('시스코', 'WORD'),
       ('오라클', 'WORD'),
       ('SAP', 'WORD'),
       ('우버', 'WORD'),
       ('에어비앤비', 'WORD'),
       ('스포티파이', 'WORD'),
       ('틱톡', 'WORD'),
       ('스냅챗', 'WORD'),
       ('위챗', 'WORD'),
       ('알리바바', 'WORD'),
       ('텐센트', 'WORD'),
       ('바이두', 'WORD'),
       ('아디다스', 'WORD'),
       ('슈프림', 'WORD'),
       ('팔라스', 'WORD'),
       ('노스페이스', 'WORD'),
       ('파타고니아', 'WORD'),
       ('아크테릭스', 'WORD'),
       ('몽클레어', 'WORD'),
       ('스톤아일랜드', 'WORD'),
       ('발렌시아가', 'WORD'),
       ('오프화이트', 'WORD'),
       ('이지부스트', 'WORD'),
       ('제프쿤스', 'WORD'),
       ('뱅앤올룹슨', 'WORD'),
       ('다이슨', 'WORD'),
       ('네스프레소', 'WORD'),
       ('필립스', 'WORD'),
       ('파나소닉', 'WORD'),
       ('샤프', 'WORD'),
       ('톰포드', 'WORD'),
       ('발렌티노', 'WORD'),
       ('생로랑', 'WORD'),
       ('돌체앤가바나', 'WORD'),
       ('불가리', 'WORD'),
       ('카르티에', 'WORD'),
       ('오메가', 'WORD'),
       ('태그호이어', 'WORD'),
       ('브라이틀링', 'WORD'),
       ('휴렛팩커드', 'WORD'),
       ('에픽게임스', 'WORD'),
       ('블리자드', 'WORD'),
       ('밸브', 'WORD'),
       ('EA스포츠', 'WORD');


INSERT INTO game_question (created_at, updated_at, question, game_type)
VALUES (NOW(), NOW(), '내 검과 심장은 데마시아의 것이다', 'SENTENCE'),
       (NOW(), NOW(), '힘을 발휘할 시간이군!', 'SENTENCE'),
       (NOW(), NOW(), '니가 사는 거라면 나도 끼지', 'SENTENCE'),
       (NOW(), NOW(), '내가 돌아왔다!', 'SENTENCE'),
       (NOW(), NOW(), '나르! 갸댜', 'SENTENCE'),
       (NOW(), NOW(), '파도가 뭘 싣고 올지 나한테 달렸죠', 'SENTENCE'),
       (NOW(), NOW(), '물이 깊으니 조심해', 'SENTENCE'),
       (NOW(), NOW(), '어둠을... 맞이하라...', 'SENTENCE'),
       (NOW(), NOW(), '모험은 역시 친구랑 같이 해야 신나는 법!', 'SENTENCE'),
       (NOW(), NOW(), '야생을 두려워하게 만들어주지', 'SENTENCE'),
       (NOW(), NOW(), '니코?! 역시 멋진 선택이야!', 'SENTENCE'),
       (NOW(), NOW(), '오직 나만이 승리로 이끌수있다', 'SENTENCE'),
       (NOW(), NOW(), '새로운 달이 떠오르고 있다', 'SENTENCE'),
       (NOW(), NOW(), '드레이븐의 리그에 오신걸 환영한다!', 'SENTENCE'),
       (NOW(), NOW(), '대재앙에 한발 앞서 가는 거다!', 'SENTENCE'),
       (NOW(), NOW(), '한번 신나게 춰볼까?', 'SENTENCE'),
       (NOW(), NOW(), '어디 한번 길을 밝혀보죠!', 'SENTENCE'),
       (NOW(), NOW(), '자 한번 붙어보자고!', 'SENTENCE'),
       (NOW(), NOW(), '내가 살아있는 한 모두 죽는다!', 'SENTENCE'),
       (NOW(), NOW(), '새벽이 밝았습니다!', 'SENTENCE'),
       (NOW(), NOW(), '오늘 밤, 사냥을 나선다', 'SENTENCE'),
       (NOW(), NOW(), '결국 모두 죽기 마련 내가 좀 거들어주지', 'SENTENCE'),
       (NOW(), NOW(), '만나서 반갑습니다~', 'SENTENCE'),
       (NOW(), NOW(), '검은 장미단은 다시 피어날 겁니다', 'SENTENCE'),
       (NOW(), NOW(), '당신의 뜻대로 싸우겠소', 'SENTENCE'),
       (NOW(), NOW(), '부러진 건 다시 붙이면 돼', 'SENTENCE'),
       (NOW(), NOW(), '이 세상을 꽁꽁 얼려주지!', 'SENTENCE'),
       (NOW(), NOW(), '나의 검은 당신의 것이오', 'SENTENCE'),
       (NOW(), NOW(), '군도의 빛을 다시 되찾으리라!', 'SENTENCE'),
       (NOW(), NOW(), '종말을 기다린다..', 'SENTENCE'),
       (NOW(), NOW(), '바위처럼 단단하게', 'SENTENCE'),
       (NOW(), NOW(), '엄청난 고통을 선사 해주겠어..', 'SENTENCE'),
       (NOW(), NOW(), '날개를 묶었다고 의지까지 꺾인건 아니야', 'SENTENCE'),
       (NOW(), NOW(), '행운은 이를 싫어 하는법이지', 'SENTENCE'),
       (NOW(), NOW(), '죄지은 자 고통받을 지어다', 'SENTENCE'),
       (NOW(), NOW(), '일단 한 대 맞아 질문은 나중에 하고', 'SENTENCE'),
       (NOW(), NOW(), '어둠에 빠진 자들을 사냥 해볼까', 'SENTENCE'),
       (NOW(), NOW(), '해체하여 지식을 습득한다', 'SENTENCE'),
       (NOW(), NOW(), '야생의 힘이다!', 'SENTENCE'),
       (NOW(), NOW(), '가장 듬직한 심장이 되어드리지', 'SENTENCE'),
       (NOW(), NOW(), '세상을 불태울 준비는 되었나?', 'SENTENCE'),
       (NOW(), NOW(), '강이 핏빛으로 물들 것이다', 'SENTENCE'),
       (NOW(), NOW(), '시스템 가동 준비 완료', 'SENTENCE'),
       (NOW(), NOW(), '영광스러운 진화에 동참하라!', 'SENTENCE'),
       (NOW(), NOW(), '영웅이라뇨! 전 그저 망치를 든 요들일 뿐이에요', 'SENTENCE'),
       (NOW(), NOW(), '휴식은..산 자를..위한..것이다...', 'SENTENCE'),
       (NOW(), NOW(), '해방이다!', 'SENTENCE'),
       (NOW(), NOW(), '마술 하나 보여줄까?', 'SENTENCE'),
       (NOW(), NOW(), '검은 안개를 홀로 상대 하지 마!', 'SENTENCE'),
       (NOW(), NOW(), '니가 가진 힘 믿을 건 오직 그뿐이야!', 'SENTENCE'),
       (NOW(), NOW(), '난 최강이다', 'SENTENCE'),
       (NOW(), NOW(), '내 숨이 붙어있는 한 고통 받게 두지 않아요', 'SENTENCE'),
       (NOW(), NOW(), '훌륭한 판단의 표본이로군!', 'SENTENCE'),
       (NOW(), NOW(), '나는 곧 내 적수의 공포이니라', 'SENTENCE'),
       (NOW(), NOW(), '네가 못 보는 힘 그것이 진짜 공포다', 'SENTENCE'),
       (NOW(), NOW(), '내 꼬리에 걸리면 곱게 죽진 못하지', 'SENTENCE'),
       (NOW(), NOW(), '내건 내가 알아서 챙겨 목숨이든 돈이든', 'SENTENCE'),
       (NOW(), NOW(), '전장으로!', 'SENTENCE'),
       (NOW(), NOW(), '한잔 하겠나?', 'SENTENCE'),
       (NOW(), NOW(), '어떤 고통을 선사 해줄까?', 'SENTENCE'),
       (NOW(), NOW(), '우리 같이.. 홀려 볼까요?', 'SENTENCE'),
       (NOW(), NOW(), '날 선택 해줄 줄은 정말 몰랐어', 'SENTENCE'),
       (NOW(), NOW(), '당연하지', 'SENTENCE'),
       (NOW(), NOW(), '내가 좋아하는 색은 봄 봄빛이지', 'SENTENCE'),
       (NOW(), NOW(), '슈리마여! 너의 황제가 돌아왔다', 'SENTENCE'),
       (NOW(), NOW(), '경외하라 나는 섬기는 이 없는 암살자다', 'SENTENCE'),
       (NOW(), NOW(), '고요한 종말에 귀가 멀게 해주마', 'SENTENCE'),
       (NOW(), NOW(), '누구도 날 막지 못해!', 'SENTENCE'),
       (NOW(), NOW(), '너도 같이 놀래? 재미있겠다!', 'SENTENCE'),
       (NOW(), NOW(), '내 날개를 타고', 'SENTENCE'),
       (NOW(), NOW(), '화살 한방으로 세상을 평정 해주지', 'SENTENCE'),
       (NOW(), NOW(), '죽음은 바람과 같지 늘 내 곁에 있으니', 'SENTENCE'),
       (NOW(), NOW(), '거미줄에선 거미만 안전하지', 'SENTENCE'),
       (NOW(), NOW(), '내가 최고가 되겠어', 'SENTENCE'),
       (NOW(), NOW(), '당신의 적을 죽이겠어요. 재미있겠네요', 'SENTENCE'),
       (NOW(), NOW(), '아무것도 남기지 않겠다!', 'SENTENCE'),
       (NOW(), NOW(), '두려워 마라 네 시체는 잘 써 주마', 'SENTENCE'),
       (NOW(), NOW(), '네놈 따위는 우리의 분노를 막을 수 없다', 'SENTENCE'),
       (NOW(), NOW(), '부러져 봐야.. 얼마나 강한 줄 알 수 있지', 'SENTENCE'),
       (NOW(), NOW(), '피비린내다... 도망쳐라!', 'SENTENCE'),
       (NOW(), NOW(), '너랑 유미랑 우리 함께 잘 해보자고', 'SENTENCE'),
       (NOW(), NOW(), '최초의 땅 아이오니아를 위하여!', 'SENTENCE'),
       (NOW(), NOW(), '날 원하잖아 그렇지?', 'SENTENCE'),
       (NOW(), NOW(), '임무를 수락하지 잠깐! 어디 가는 거라고?', 'SENTENCE'),
       (NOW(), NOW(), '내 의지로 여기서 끝을 보겠노라!', 'SENTENCE'),
       (NOW(), NOW(), '댄스파티에 내가 빠질 순 없지', 'SENTENCE'),
       (NOW(), NOW(), '가시덤불에 포근히 안겨봐', 'SENTENCE'),
       (NOW(), NOW(), '딱 봐도 잘 싸우게 생기지 않았어?', 'SENTENCE'),
       (NOW(), NOW(), '바람의 힘을 마음껏 부리세요', 'SENTENCE'),
       (NOW(), NOW(), '자 한번 해보자고!', 'SENTENCE'),
       (NOW(), NOW(), '보이지 않는 검이 가장 무서운 법', 'SENTENCE'),
       (NOW(), NOW(), '난... 해방될 것이다!', 'SENTENCE'),
       (NOW(), NOW(), '나는 더 나은 미래를 위해 싸운다', 'SENTENCE'),
       (NOW(), NOW(), '야호 재밌겠다 그치?', 'SENTENCE'),
       (NOW(), NOW(), '뻥이요!', 'SENTENCE'),
       (NOW(), NOW(), '학살의 현장에서 난 피어오른다', 'SENTENCE'),
       (NOW(), NOW(), '그리할 줄 알고 있었소', 'SENTENCE'),
       (NOW(), NOW(), '하핫! 신나게 놀아볼까?', 'SENTENCE'),
       (NOW(), NOW(), '세계의 종말을 원한다고? 좋아!', 'SENTENCE'),
       (NOW(), NOW(), '항상 정신을 집중하세요', 'SENTENCE'),
       (NOW(), NOW(), '정교함이 없다면 망나니일 뿐', 'SENTENCE'),
       (NOW(), NOW(), '힘의 균형은 유지되어야 한다', 'SENTENCE'),
       (NOW(), NOW(), '날 막을 해독제는 없을걸?', 'SENTENCE'),
       (NOW(), NOW(), '사냥꾼이냐? 사냥감이냐?', 'SENTENCE'),
       (NOW(), NOW(), '변화란 좋은 거야', 'SENTENCE'),
       (NOW(), NOW(), '폭력은 모든 것을 해결 해주지', 'SENTENCE'),
       (NOW(), NOW(), '배신자에겐 죽음뿐!', 'SENTENCE'),
       (NOW(), NOW(), '눈은 절대 거짓말을 하지 않지', 'SENTENCE'),
       (NOW(), NOW(), '이 사건은 내가 맡죠', 'SENTENCE'),
       (NOW(), NOW(), '모두가 내 전능함을 경외하리라!', 'SENTENCE'),
       (NOW(), NOW(), '만찬의 시간이다!', 'SENTENCE'),
       (NOW(), NOW(), '상황 파악 끝 최고의 전투기까지 있다고!', 'SENTENCE'),
       (NOW(), NOW(), '정의의 날개로', 'SENTENCE'),
       (NOW(), NOW(), '너도 내 진가를 알게 되겠군!', 'SENTENCE'),
       (NOW(), NOW(), '희망의 빛이 보이십니까? 그게 바로 접니다', 'SENTENCE'),
       (NOW(), NOW(), '칼에 살고 칼에 죽는다', 'SENTENCE'),
       (NOW(), NOW(), '마법의 베틀로 바위를 엮어내리라', 'SENTENCE'),
       (NOW(), NOW(), '트롤 한판 해볼까?', 'SENTENCE'),
       (NOW(), NOW(), '일단 한번 쏘고 나면 또 쏘고 싶을 거에요!', 'SENTENCE'),
       (NOW(), NOW(), '손쉬운 사냥이 되겠군!', 'SENTENCE'),
       (NOW(), NOW(), '행운의 여신이 내게 미소를 짓는군', 'SENTENCE'),
       (NOW(), NOW(), '죽거나 죽이거나 약육강식은 그런 거지', 'SENTENCE'),
       (NOW(), NOW(), '티모대위 명을 받들겠습니다', 'SENTENCE'),
       (NOW(), NOW(), '모두... 빠트려주마', 'SENTENCE'),
       (NOW(), NOW(), '전쟁에서 우린 다시 태어난다!', 'SENTENCE'),
       (NOW(), NOW(), '겨뤄 볼 만한 상대... 어디 없나?', 'SENTENCE'),
       (NOW(), NOW(), '내가 혼내줄게!', 'SENTENCE'),
       (NOW(), NOW(), '정말 현명한 선택이에요!', 'SENTENCE'),
       (NOW(), NOW(), '그림자 군도의 위력을 똑똑히 봐라!', 'SENTENCE');

INSERT INTO game_question (created_at, updated_at, question, game_type)
VALUES (NOW(), NOW(), '밥은 먹고 다니냐?', 'SENTENCE'),
       (NOW(), NOW(), '고마해라, 많이 묵다 아이가', 'SENTENCE'),
       (NOW(), NOW(), '넌 나에게 모욕감을 줬어', 'SENTENCE'),
       (NOW(), NOW(), '니가 가라, 하와이', 'SENTENCE'),
       (NOW(), NOW(), '고마해라. 마이 무따 아이가', 'SENTENCE'),
       (NOW(), NOW(), '느그 아부지 머하시노', 'SENTENCE'),
       (NOW(), NOW(), '묻고 더블로 가!', 'SENTENCE'),
       (NOW(), NOW(), '내가 빙다리 핫바지로 보이냐?', 'SENTENCE'),
       (NOW(), NOW(), '아들아, 너는 계획이 다 있구나', 'SENTENCE'),
       (NOW(), NOW(), '꼭 그렇게 다 가져가야만 속이 후련했냐!', 'SENTENCE'),
       (NOW(), NOW(), '어찌, 내가 왕이 될 상인가?', 'SENTENCE'),
       (NOW(), NOW(), '초원이 다리는 백만불짜리 다리', 'SENTENCE'),
       (NOW(), NOW(), '거 죽기 딱 좋은 날씨네', 'SENTENCE');

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
       (NOW(), NOW(), 'public class Main {
	public static void main(String[] args) throws IOException {
		new Main().solve();
	}
	public void solve() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(br.readLine());
		int[] code = new int[n + 1];
		code[1] = 1;
		code[2] = 1;
		for(int i = 3; i <= n; i++) {
			code[i] = code[i - 1] + code[i - 2];
		}
		System.out.println(code[n] + " " + (n - 2));
	}
}', 'CODE'),
       (NOW(), NOW(), 'public class Main {
  public static void main(String[] args) throws java.lang.Exception {
    Stack < Integer > stk = new Stack < > ();
    if (stk.empty()) {
      stk.push(2);
      stk.push(3);
    }
    if (!stk.empty()) {
      if (stk.peek() == 3)
        stk.pop();
    }
    if (stk.search(3) == -1) {
      System.out.println("3 is poped");
    }
  }
}', 'CODE'),
       (NOW(), NOW(), 'int flag = 1;
int val = 0;
char[] chars = s.toCharArray();

for (char c: chars) {
  if (c == ''-'') {
    flag = -1;
    continue;
  }
  val *= 10;
  val += c - ''0'';
}
val *= flag;', 'CODE'),
       (NOW(), NOW(), 'public class Node implements Comparable<Node> {
    int index;
    int value;

    public Node(int index, int value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(Node newNode) {
        return newNode.value - value;
        return value - newNode.value;
    }
}', 'CODE'),
       (NOW(), NOW(), 'public class Main {
	public static void main(String[] args) {
	    Scanner scan = new Scanner(System.in);
	    int total = scan.nextInt();
	    int minCoinCnt = 0;
	    int coins[] = {500, 100, 50, 10};
	    for (int coin : coins){
	        minCoinCnt += (total/coin);
	        total %= coin;
	    }
		System.out.println("result = " + minCoinCnt);
	}
}', 'CODE'),
       (NOW(), NOW(), 'public class Main {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    int n = scan.nextInt();
    int k = scan.nextInt();
    int result = 0;

    while (true) {
      result += (n % k) + 1;
      n /= k;
      if (n < k) break;
    }
    result += (n - 1);
    System.out.println("result = " + result);
  }
}', 'CODE'),
       (NOW(), NOW(), 'import java.util.Scanner;
public class Main {
  static long mod = 10007;
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    long D[] = new long[1001];
    D[1] = 1;  //길이가 2*1일때 타일 경우의 수
    D[2] = 2;  //길이가 2*2일때 타일 경우의 수
    for(int i=3; i<=N; i++){
      D[i] = (D[i-1] + D[i-2])%mod;
    }
    System.out.println(D[N]);
  }
}', 'CODE'),
       (NOW(), NOW(), 'public class Main {
  static int N;
  static int D[];
  public static void main(String[] args) throws Exception {
    Scanner sc = new Scanner(System.in);
    N = sc.nextInt();
    D = new int[N + 1];
    D[1] = 0;
    for (int i = 2; i <= N; i++) {
      D[i] = D[i-1]+1;
      if(i%2==0)D[i]=Math.min(D[i], D[i/2]+1);
      if(i%3==0)D[i]=Math.min(D[i], D[i/3]+1);
    }
    System.out.println(D[N]);
  }
}', 'CODE'),
       (NOW(), NOW(), 'public class P1947_선물전달 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    long mod = 1000000000;
    long D[] = new long[1000001];
    D[1] = 0;
    D[2] = 1;
    for(int i=3; i<=N; i++){
      D[i] = (i-1)*(D[i-1]+D[i-2])%mod;
    }
    System.out.println(D[N]);
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
       (NOW(), NOW(), 'const items = ["Apple", "Orange", "Banana", "Mango"];
const list = document.createElement("ul");
items.forEach(item => {
  const listItem = document.createElement("li");
  listItem.textContent = item;
  list.appendChild(listItem);
});
document.body.appendChild(list);', 'CODE'),
       (NOW(), NOW(), 'fetch(''https://jsonplaceholder.typicode.com/posts'')
  .then(response => response.json())
  .then(data => {
    data.forEach(post => {
      console.log(post.title);
    });
  })
  .catch(error => console.error(''Error:'', error));', 'CODE'),
       (NOW(), NOW(), 'async function fetchData() {
  try {
    const response = await fetch(''https://jsonplaceholder.typicode.com'');
    const data = await response.json();
    data.forEach(post => {
      console.log(post.title);
    });
  } catch (error) {
    console.error(''Error:'', error);
  }
}
fetchData();', 'CODE'),
       (NOW(), NOW(), 'const addButton = document.getElementById("addButton");
const inputField = document.getElementById("todoInput");
const todoList = document.getElementById("todoList");

addButton.addEventListener("click", () => {
  const todoText = inputField.value;
  const listItem = document.createElement("li");
  listItem.textContent = todoText;
  listItem.addEventListener("click", () => {
    listItem.style.textDecoration = "line-through";
  });
  todoList.appendChild(listItem);
  inputField.value = '''';
});', 'CODE'),
       (NOW(), NOW(), 'function countVowels(str) {
  const vowels = ''aeiouAEIOU'';
  let count = 0;
  for (let char of str) {
    if (vowels.includes(char)) count++;
  }
  return count;
}

console.log(countVowels(''hello world''));', 'CODE'),
       (NOW(), NOW(), 'function countVowels(str) {
  const vowels = ''aeiouAEIOU'';
  let count = 0;
  for (let char of str) {
    if (vowels.includes(char)) count++;
  }
  return count;
}
console.log(countVowels(''hello tikitaza''));', 'CODE'),
       (NOW(), NOW(), 'function longestWordLength(sentence) {
  const words = sentence.split('' '');
  let maxLength = 0;
  for (let word of words) {
    maxLength = Math.max(maxLength, word.length);
  }
  return maxLength;
}
console.log(longestWordLength(''The quick brown fox jumps over the lazy dog''));', 'CODE'),
       (NOW(), NOW(), 'let randomNumber = Math.floor(Math.random() * 100) + 1;

const guesses = document.querySelector(".guesses");
const lastResult = document.querySelector(".lastResult");
const lowOrHi = document.querySelector(".lowOrHi");

const guessSubmit = document.querySelector(".guessSubmit");
const guessField = document.querySelector(".guessField");

let guessCount = 1;
let resetButton;', 'CODE'),
       (NOW(), NOW(), 'function setGameOver() {
  guessField.disabled = true;
  guessSubmit.disabled = true;
  resetButton = document.createElement("button");
  resetButton.textContent = "Start new game";
  document.body.append(resetButton);
  resetButton.addEventListener("click", resetGame);
}', 'CODE'),
       (NOW(), NOW(), 'for (let i = 0; i < 100; i++) {
  ctx.beginPath();
  ctx.fillStyle = "rgb(255 0 0 / 50%)";
  ctx.arc(
    random(canvas.width),
    random(canvas.height),
    random(50),
    0,
    2 * Math.PI,
  );
  ctx.fill();
}', 'CODE'),
       (NOW(), NOW(), 'function lCat(cat) {
  return cat.startsWith("L");
}

const cats = ["Leopard", "Serval", "Jaguar", "Tiger", "Caracal", "Lion"];

const filtered = cats.filter(lCat);

console.log(filtered);', 'CODE');
