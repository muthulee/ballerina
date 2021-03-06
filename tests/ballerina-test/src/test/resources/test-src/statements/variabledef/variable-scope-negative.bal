

function testVariableIfScope () (int) {
    int a = 90;
    int b = 50;
    if (a > 20) {
        int c = 20;
    } else {
        int k = 60;
        if (b < 100) {
            k = b + 30;
        }
    }
    return k;
}

function testVariableElseScope() (int) {
    int a = 10;
    if(a > 20) {
        a = 50;
    } else {
        int b = 30;
    }

    return b;
}

function testVariableWhileScope() {
    int a = 0;
    while( a < 5) {
        a = a + 1;
        int b = a + 20;
    }
    int sum = b;
}

service<DummyService> myService {
    int a = 20;

    myResource1(string s) {
        string res = "abc";
        int b = a + 50;
    }

    myResource2(string s) {
        string res = "abc";
        int c = b + 50;
    }
}

struct DummyEndpoint {}

function <DummyEndpoint s> init (struct {} conf)  {
}

struct DummyService {}

function <DummyService s> getEndpoint() returns (DummyEndpoint) {
    return null;
}
