package dms.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtManager {

	/**
	 * 1��ѡ��ǩ�����㷨 2������ǩ������Կ 3������Token��Ϣ 4�������㷨����Կ����Token
	 * 
	 * @param userId
	 *            �û�Id
	 * @param userName
	 *            �û���
	 * @param roleId
	 *            ��ɫId
	 * @param roleName
	 *            ��ɫ��
	 * @param userGroupId
	 *            �û�Ⱥ��Id
	 * @param userGroupName
	 *            �û�Ⱥ����
	 * @return
	 */
	public static String createToken(int userId, String userName, int roleId, String roleName, int userGroupId,
			String userGroupName) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] secretBytes = DatatypeConverter.parseBase64Binary(Constants.tokenSecret);
		Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("userId", userId);
		claims.put("userName", userName);
		claims.put("roleId", roleId);
		claims.put("roleName", roleName);
		claims.put("userGroupId", userGroupId);
		claims.put("userGroupName", userGroupName);
		JwtBuilder builder = Jwts.builder().setClaims(claims).setId("tokenid").setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
				.signWith(signatureAlgorithm, signingKey);
		return builder.compact();
	}

	/**
	 * ����token����
	 * 
	 * @param token
	 * @return
	 */
	public static Claims parseToken(String token) {
		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(Constants.tokenSecret))
				.parseClaimsJws(token).getBody();
	}
}
