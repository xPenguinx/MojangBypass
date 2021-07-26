package net.penguin.transformer;

import java.security.ProtectionDomain;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import net.minecraft.launchwrapper.IClassTransformer;

public class Transformer implements IClassTransformer {

	private final Logger log = LogManager.getLogger(Transformer.class);

	private final List<String> clazz = Lists.newArrayList("com.mojang.patchy.BlockedServers", "io.netty.bootstrap.Bootstrap");

	public Transformer() {}
	
	/**
	 * Github: https://github.com/videogame-hacker/MojangBlacklistBypass
	 */

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		name = name.replace("/", ".");
		if (clazz.contains(name))
		{
			try {
				log.info("Transforming Class {}", name);
				ClassPool pool = ClassPool.getDefault();
				pool.appendClassPath(new ByteArrayClassPath(name, basicClass));

				CtClass ctClass = pool.get(name);
				CtMethod method = ctClass.getMethod("isBlockedServerHostName", "(Ljava/lang/String;)Z");
				method.setBody("{ return false; }");

				return ctClass.toBytecode();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return basicClass;
	}

}