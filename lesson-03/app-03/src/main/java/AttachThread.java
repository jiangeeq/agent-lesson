import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jiangpeng
 * @date 2019/12/1 0001
 */
public class AttachThread extends Thread {
    /**
     * 记录程序启动时的 VM 集合
     */
    private final List<VirtualMachineDescriptor> listBefore;
    /**
     要加载的agent.jar
     */
    private final String jar;

    private AttachThread(String attachJar, List<VirtualMachineDescriptor> vms) {
        listBefore = vms;
        jar = attachJar;
    }

    @Override
    public void run() {
        VirtualMachine vm;
        List<VirtualMachineDescriptor> listAfter;
        int count = 0;

        try {
            while (true) {
                listAfter = VirtualMachine.list();
                vm = hasNewVm(listAfter);

                if(vm == null){
                    System.out.println("没有新jvm程序，请手动指定java pid");
                    try{
                        vm = VirtualMachine.attach("7716");
                    } catch (Exception e){
                        System.out.println("拒绝访问 Disconnected from the target VM");
                    }
                }

                Thread.sleep(1000);
                System.out.println(count++);
                if (null != vm || count >= 100) {
                    break;
                }
            }
            Objects.requireNonNull(vm).loadAgent(jar);
            vm.detach();
        } catch (Exception e) {
            System.out.println("异常：" + e);
        }
    }

    /**
     * 判断是否有新的 JVM 程序运行
     */
    private VirtualMachine hasNewVm(List<VirtualMachineDescriptor> listAfter) throws IOException, AttachNotSupportedException {
        for (VirtualMachineDescriptor vmd : listAfter) {
            if (!listBefore.contains(vmd)) {
                // 如果 VM 有增加，，我们开始监控这个 VM
                System.out.println("有新的 vm 程序："+ vmd.displayName());
                return VirtualMachine.attach(vmd);
            }
        }
        return null;
    }

    public static void main(String[] args)  {
        final String agentPath = "D:\\learning\\lesson-03\\agent-03\\target\\agent-03-1.0-SNAPSHOT.jar";
        new AttachThread(agentPath  , VirtualMachine.list()).start();
    }
}
