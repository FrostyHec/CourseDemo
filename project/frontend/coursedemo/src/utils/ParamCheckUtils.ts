import { InvalidParamException } from '@/utils/Exceptions'

export class Ex{

  static check(condition: boolean, exception: string | InvalidParamException) {
    if (typeof exception === 'string') {
      exception = new InvalidParamException(exception);
    }

    if (!condition) {
      throw exception;
    }
  }
}