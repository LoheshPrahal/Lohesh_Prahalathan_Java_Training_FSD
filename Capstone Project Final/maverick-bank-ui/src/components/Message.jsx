// Error / success message of a screen
function Message({ errMsg, successMsg }) {
    return (
        <div>
            {errMsg && <div className="alert alert-danger py-2" role="alert">{errMsg}</div>}
            {successMsg && <div className="alert alert-success py-2" role="alert">{successMsg}</div>}
        </div>
    )
}

export default Message
