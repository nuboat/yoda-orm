package in.norbor.yoda.jtype

import org.mindrot.jbcrypt.BCrypt

/**
  * Created by Peerapat A, Oct 10, 2017
  */
case class Jbcrypt(hash: String) {

  def isMatch(plaintext: String): Boolean = (plaintext, hash) match {
    case (null, null) | (null, "") | ("", null) | ("", "") => false
    case _ => BCrypt.checkpw(plaintext, hash)
  }

}

object Jbcrypt {

  def build(plain: String): Jbcrypt = if (plain == null || plain.isEmpty)
    null
  else
    Jbcrypt(BCrypt.hashpw(plain, BCrypt.gensalt()))

}
