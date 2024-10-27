export class InvalidParamException extends Error{
  public cause: Object | null

  constructor(message: string, cause: Object|null = null) {
    super(message)
    this.cause = cause
  }
}

export class InternalException extends Error{
  public cause: Object | null

  constructor(message: string, cause: Object|null = null) {
    super(message)
    this.cause = cause
  }
}