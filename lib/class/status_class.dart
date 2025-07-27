class StatusBase {
  factory StatusBase.fromMap(String status) {
    if (status == "success") {
      return Success();
    } else {
      return Error(errorMessage: status);
    }
  }
}

class Success implements StatusBase {}

class Error implements StatusBase {
  final errorMessage;
  Error({required this.errorMessage});
}
