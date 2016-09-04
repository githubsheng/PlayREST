import com.google.inject.AbstractModule

import services.{ UserRepoImpl, UserRepo }

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[UserRepo]).to(classOf[UserRepoImpl])
  }

}
