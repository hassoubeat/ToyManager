// 2つの日付の間の経過日数を得る
function keikaNissu(fromDate, toDate) {
    var dayms = 24 * 60 * 60 * 1000;
    return Math.floor((toDate.getTime()-fromDate.getTime())/dayms);
}
