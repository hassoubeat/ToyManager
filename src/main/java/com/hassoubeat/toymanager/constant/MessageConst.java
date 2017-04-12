/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.constant;

/**
 *
 * @author hassoubeat
 */
public enum MessageConst {
    // MIR(ToyManager側で発生するインフォメーションメッセージ)
    GEN_USER_AUTH_CODE("MIR_01000001", "ユーザID登録認証コードを発行します。"),
    SUCCESS_USER_AUTH_CODE("MIR_01000002", "ユーザID登録認証に成功しました。"),
    SUCCESS_SIGNUP("MIR_01000003", "アカウントを作成しました。"),
    SUCCESS_SEND_AUTH_CODE_MAIL("MIR_01000004", "認証コード通知メールを送信しました。"),
    SUCCESS_LOGIN("MIR_01000005", "ログインに成功しました。"),
    SUCCESS_EDIT_PASSWORD("MIR_01000006", "パスワードの変更に成功しました。"),
    SUCCESS_REMIND_PASSWORD("MIR_01000007", "パスワードのリマインドを実行しました。"),
    SUCCESS_AUTHORITY("MIR_01000008", "認証が完了しました。"),
    SUCCESS_LOGOUT("MIR_01000009", "ログアウトが完了しました。"),
    SELECT_TOY("MIR_01000010", "Toyを選択しました。"),
    SELECT_EVENT("MIR_01000011", "イベントを選択しました。"),
    SUCCESS_EVENT_CREATE("MIR_01000012", "イベントとDiffイベントの登録が完了しました。"),
    SUCCESS_EVENT_EDIT("MIR_01000013", "イベントの更新が完了しました。"),
    SUCCESS_EVENT_LOGIC_REMOVE("MIR_01000014", "イベントの論理削除が完了しました。"),
    SUCCESS_EVENT_REMOVE("MIR_01000015", "イベントの削除が完了しました。"),
    SUCCESS_TOY_ACCOUNT_TYING("MIR_01000016", "アカウントにToyの紐付けを完了しました。"),
    SUCCESS_TOY_ACCOUNT_TYING_CANCEL("MIR_01000017", "アカウントからToyの紐付けを解除しました。"),
    SUCCESS_ACCESS_TOKEN_GENERATE("MIR_01000018", "アクセストークンの生成を実施しました。"),
    SUCCESS_TOY_EDIT("MIR_01000019", "Toyの更新が完了しました。"),
    SUCCESS_ACCESS_FILTER_APPROVAL("MIR_01000020", "アクセスフィルターを承認しました。"),
    SUCCESS_ACCESS_FILTER_REJECT("MIR_01000021", "アクセスフィルターを拒否しました。"),
    SUCCESS_FACET_CREATE("MIR_01000022", "ファセットの登録が完了しました。"),
    SUCCESS_FACET_EDIT("MIR_01000023", "ファセットの変更が完了しました。"),
    SUCCESS_FACET_REMOVE("MIR_01000024", "ファセットの削除が完了しました。"),
    SUCCESS_S3_OBJECT_UPLOAD("MIR_01000025", "AmazonS3へオブジェクトをアップロードしました。"),
    SUCCESS_S3_OBJECT_REMOVE("MIR_01000026", "AmazonS3のオブジェクトを削除しました。"),
    SUCCESS_FACET_EVENT_CREATE("MIR_01000027", "ファセットイベントの登録が完了しました。"),
    SUCCESS_FACET_EVENT_EDIT("MIR_01000028", "ファセットイベントの変更が完了しました。"),
    SUCCESS_FACET_EVENT_REMOVE("MIR_01000029", "ファセットイベントの削除が完了しました。"),
    SUCCESS_TOY_VOICE_TYPE_EDIT("MIR_01000030", "Toyのボイスタイプを変更しました。"),
    SUCCESS_TOY_FACET_CREATE("MIR_01000031", "Toyにファセットへの追加が完了しました。"),
    SUCCESS_FACET_EVENT_COPY("MIR_01000032", "ファセットイベントからイベントのコピーを完了しました。"),
    SUCCESS_TOY_FACET_REMOVE("MIR_01000033", "Toyからファセットを削除しました。"),
    SUCCESS_TOY_FACET_UPDATE("MIR_01000034", "ファセットのアップデートが完了しました。"),
    
    // MER(ToyManager側で発生する想定内エラー系)
    ALREADY_REGISTED_USER("MER_01000001", "入力したメールアドレスは既に登録されています。"),
    FAILED_USER_AUTH_CODE("MER_01000002", "ユーザID登録認証コードが誤っています。"),
    FAILED_SIGNUP("MER_01000003", "アカウント作成に失敗しました。もう一度最初からやり直してください。"),
    INVALID_SCREEN_TRANSITION("MER_01000004", "不正な画面遷移が行われました。トップ画面からやり直して下さい。"),
    INVALID_USERID_OR_PASSWORD("MER_01000005", "入力されたID/パスワードのどちらかが誤っています。"),
    INVALID_USERID("MER_01000006", "入力されたIDが誤っています。"),
    INVALID_PASSWORD("MER_01000007", "入力されたパスワードが誤っています。"),
    LOGICAL_DELETE_USER("MER_01000008", "入力されたアカウントは現在使用することができません。"),
    NOT_FOUND_USER("MER_01000009", "入力されたユーザIDは存在しません。"),
    NOT_LOGIN("MER_01000010", "ログインされていません。"),
    UN_AUTHORITY("MER_01000011", "権限がありません。"),
    INVALID_CHECK_PASSWORD("MER_01000012", "確認用パスワードが誤っています。"),
    ALREADY_REMOVE_EVENT("MER_10000013", "選択したイベントは既に存在しません。"),
    DATE_CONTRADICTION("MER_10000014", "終了日付より開始日付の方が過去の値が設定されています。"),
    INVALID_ROT_NUMBER_OR_PASSWORD("MER_10000015", "入力されたロットナンバー/パスワードのどちらかが誤っています。"),
    ALREADY_TYING_ROT_NUMBER("MER_10000016", "入力されたロットナンバーは既に別のアカウントに登録されています。"),
    S3_FILE_UPLOAD_FAILED("MER_10000017", "AmazonS3へのファイルアップロードに失敗しました。"),
    MISMATCH_FACET_NAME("MER_01000018", "ファセット名とファセットプログラムの名前が一致しません。"),
    ALREADY_USED_FACET_NAME("MER_01000019", "ファセット名は既に利用されています。"),
    
    
    // MCR(ToyManager側で発生する想定外エラー系)
    SYSTEM_ERROR("MCR_01000001", "システムエラーが発生しました。お手数ですが、操作をやりなおしてください。"),
    FAILED_SEND_MAIL("MCR_01000002", "メールの送信に失敗しました。"),
    FAILED_SEND_AUTH_CODE_MAIL("MCR_01000003", "認証コード通知メールの送信に失敗しました。"),
    FAILED_SEND_REMIND_PASSWORD_MAIL("MCR_01000004", "パスワードリマインド通知メールの送信に失敗しました。"),
    
    // MRIR(ToyManagerのRESTAPIで発生するインフォメーションメッセージ)
    REST_SUCCESS_WEB_API_ACCESS_FILTER_CREATE("MRIR_01000001", "アクセスフィルターを新規登録しました。"),
    
    // MRER(ToyManagerのRESTAPIで発生する想定内エラー系)
    REST_INVALID_PARAM("MRER_01000001", "不正な認証情報が送信されました。"),
    REST_ACCESS_FILTER_UN_APPROVAL("MRER_01000002", "アクセスフィルターが未承認です。"),
    REST_ACCESS_TOKEN_EXPIRED("MRER_01000003", "アクセストークンの有効期限切れです。"),
    REST_FAILED_AUTHORIZATION("MRER_01000004", "認証に失敗しました。"),
    
    // MRCR(ToyManagerのRESTAPIで発生する想定外エラー系)
    REST_SYSTEM_ERROR("MRCR_01000001", "システムエラーが発生しました。")
    ;
    
    private final String id;
    private final String message;
    
    private MessageConst(final String id, final String message) {
        this.id = id;
        this.message = message;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getMessage() {
        return this.message;
    }
}
