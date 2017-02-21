// 渡された文字列を数値として扱って足し算する
function sum(param1, param2) {
    return parseInt(param1) + parseInt(param2);
}

// 渡された文字列を数値として扱って引き算する
function subtraction(param1, param2) {
    return parseInt(param1) - parseInt(param2);
}

// 渡された文字列を数値として扱ってANDビット演算を行う
function bitAnd(param1, param2) {
    return parseInt(param1) & parseInt(param2);
}

// 渡された文字列を数値として扱ってORビット演算を行う
function bitOr(param1, param2) {
    return parseInt(param1) | parseInt(param2);
}

// 渡された文字列を数値として扱い、NOTビット演算を行う
function bitNot(param) {
    return parseInt(~ param);
}

// 渡された文字列を数値として扱ってXORビット演算を行う
function bitXor(param1, param2) {
    return parseInt(param1) ^ parseInt(param2);
}

// 渡された文字列を数値として扱って指定したビットが立っていた場合、Trueを返す
function bitCheck(param1, param2) {
    if ((parseInt(param1) & parseInt(param2)) > 0) {
        console.log("param1" + param1);
        console.log("param1" + param2);
        return true;
    } 
    return false;
}

// 渡された文字列を２進数/１０進数/１６進数に変換して表示する
function show2_10_16(param) {
    console.log("2:" + parseInt(param).toString(2));
    console.log("10:" + parseInt(param));
    console.log("16:" + parseInt(param).toString(16));
}


// 2つの日付の間の経過日数を得る
function keikaNissu(fromDate, toDate) {
    var dayms = 24 * 60 * 60 * 1000;
    return Math.floor((toDate.getTime()-fromDate.getTime())/dayms);
}
